package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.XMLException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/*package-private*/ class VerticesGraph {


    private static final int truc = 13;

    public static void main(String args[]){
        int n = truc;
        int k = 2*n+1;
        ArrayList<ArrayList<Double>> len = new ArrayList<>();

        for(int i = 0; i<k; ++i){
            len.add(new ArrayList<>());
            for(int j=0; j<k; ++j){
                len.get(i).add(j, Math.abs(Math.cos(i) + Math.sin(j)) + 0.001);
            }
        }

        VerticesGraph G = new VerticesGraph(n, len);
        long pickUps = 0;
        long a = 1;
        for(int truc = 0; truc < n; ++truc){
            pickUps += a;
            a *= 4;
        }
        pickUps *= 2;

        //System.out.println(pickUps);

        var debut = System.nanoTime();
        SubResult subResult = G.resolveSubProblem(0, pickUps);
        ArrayList<Integer> L = G.makePath(subResult);
        var fin = System.nanoTime();
        for(int i : L){
            System.out.print(i);
            System.out.print("  ");
        }
        System.out.println(" ");
        System.out.println((fin - debut)*0.000000001 + "  s");
        System.out.println((float)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576. + "  mo");

//        var debut = System.nanoTime();
//        var L = G.naiveRound();
//        var fin = System.nanoTime();
//        for(Vertex v : L){
//            System.out.print(v.getId());
//            System.out.print("  ");
//        }
//        System.out.println(" ");
//        System.out.println((fin - debut)*0.000000001);
    }


    public VerticesGraph(int n, ArrayList<ArrayList<Double>> len){
        powerSetSize = 0b1 << (2*n+1);
        lengths = len;
        subResults = new HashMap<>();
    }

    /** The speed of the cyclist in meters per second */
    private static final int CYCLIST_SPEED = (int) (15. / 3.6); // TODO @Felix: mettre ces constantes dans la classes contenant toutes les constantes

    /** 2^(2n+1) where n is the number of deliveries, N is the number of subsets of the vertices */
    private long powerSetSize;

    /** The deliveries that the cyclist need to deliver */
    private DeliveryMap deliveries;

    /** The results from the Dijkstra's algorithms */
    private HashMap<Long, PathsFromVertex> pathsFromVertices;

    /** The results from the Dijkstra's algorithms */
    private ArrayList<ArrayList<Double>> lengths;

    /** We will use dynamic programing, this is where we store the sub results */
    private HashMap<Long, SubResult> subResults;

    /**
     * Let n be the number of deliveries we want to process
     * Creates a graph G with 2n+1 vertices : one for the warehouse and two for each delivery (the pick up and the drop off)
     * The vertices are indexed from 0 to 2n : the warehouse is associated to 0
     * the pick up of the i-th delivery is associated to 2i+1
     * the drop off of this same delivery is associated to 2i+2
     *
     * @param deliveries        a delivery map we want to process
     * @param pathsFromVertices the results of Dijkstra's algorithms from all nodes in deliveries
     */
    /*package-private*/ VerticesGraph(DeliveryMap deliveries, HashMap<Long, PathsFromVertex> pathsFromVertices) {
        int n = deliveries.getSize();
        powerSetSize = 0b1 << (2 * n + 1); //makes 2^(2n+1), thanks Pacôme
        this.deliveries = deliveries;
        this.pathsFromVertices = pathsFromVertices;

        lengths = new ArrayList<>(2 * n + 1);
        lengths.add(new ArrayList<>(2 * n + 1));
        for (int i = 0; i < 2 * n + 1; ++i) {
            lengths.add(new ArrayList<>(2 * n + 1));
            long id1 = idFromIndex(i);
            for (int j = 0; j < 2 * n + 1; ++j) {
                long id2 = idFromIndex(j);
                double len = pathsFromVertices.get(id1).getLength(id2);
                lengths.get(i).add(len);
            }
        }

        subResults = new HashMap<>();
    }

    private int deliveryIdFromIndex(int index) {
        Delivery delivery = deliveries.getDelivery((index - 1) / 2);
        if (index % 2 == 1) {
            return delivery.getPickUpDuration();
        }
        return delivery.getId();
    }

    /**
     * @param index a vertex in G
     * @return the id of the associated map node
     */
    private long idFromIndex(int index) {
        if (index == 0) {
            return deliveries.getWarehouseNodeId();
        }
        Delivery delivery = deliveries.getDelivery((index - 1) / 2);
        if (index % 2 == 1) {
            return delivery.getPickUpNodeId();
        }
        return delivery.getDropOffNodeId();
    }

    /**
     * @param index a vertex in G
     * @return the duration of the associated vertex
     */
    private int durationFromIndex(int index) {
        Delivery delivery = deliveries.getDelivery((index - 1) / 2);
        if (index % 2 == 1) {
            return delivery.getPickUpDuration();
        }
        return delivery.getDropOffDuration();
    }

    /**
     * Calculates the key associated with a sub problem in the dynamic programming process
     * This key will be used in the map subResults to store the sub result of this sub problem
     * A sub problem is a couple (v, S) where v is a vertex of G and S a subset of the vertices of G that does not contain v
     * The goal is to find the shortest hamiltonian path of Su{v} whose first vertex is v
     *
     * @param start   the vertex v of the previous explanation
     * @param setCode a number that represents S, more precisely the sum of all 2^k for k in S
     * @return the key associated with (v, S)
     */
    private Long subProblemKey(int start, long setCode) {
        return setCode + powerSetSize * start;
    }

    /**
     * Returns the result of the sub problem (start, S) where S is the subset coded by setCode
     *
     * @param start   the starting vertex of our path
     * @param setCode the subSet that need to be explored
     * @return the sub result of the sub problem (start, s)
     */
    private SubResult resolveSubProblem(int start, long setCode) {
        long key = subProblemKey(start, setCode);
        if (subResults.containsKey(key)) { //this sub problem has been solved yet, we don't solve it again
            return subResults.get(key);
        }
        if (setCode == 0) { //stop case, when S is the empty set
            SubResult subResult = new SubResult(0, 0);
            //the first 0 can be replaced by lengths.get(start).get(0) if we want the cyclist to come back
            subResults.put(key, subResult);
            return subResult;
        }
        double bestLength = -1;
        long nextKey = 0;
//        int n = deliveries.getSize();
        int n = truc;
        long a = 2; //will be 2^k, used to add and remove elements from the set
        long copy = setCode / 2; //will be setCode/2^k, used to get the elements of the set
        for (int k = 1; k < 2 * n + 1; ++k) {
            if ((copy & 1) == 1) { //k belongs to the set
                if ((k & 1) == 1) { //k is a pick up
                    //we remove the pick up from the set and add his associated drop off instead
                    SubResult candidate = resolveSubProblem(k, setCode + a);
                    //setCode+a is in fact setCode-a+2a, i.e.setCode-2^k+2^(k+1)
                    //that means we remove int k from the set and add k+1 instead
                    double candidateLength = candidate.getLength() + lengths.get(start).get(k);
                    if (bestLength == -1 || bestLength > candidateLength){
                        bestLength = candidateLength;
                        nextKey = subProblemKey(k, setCode + a);
                    }
                } else { //k is a drop off
                    //we remove the drop off from the set
                    SubResult candidate = resolveSubProblem(k, setCode - a);
                    double candidateLength = candidate.getLength() + lengths.get(start).get(k);
                    if (bestLength == -1 || bestLength > candidateLength){
                        bestLength = candidateLength;
                        nextKey = subProblemKey(k, setCode - a);
                    }
                }
            }
            a = a << 1;
            copy = copy >> 1;
        }
        SubResult subResult = new SubResult(bestLength, nextKey);
        subResults.put(key, subResult);
        //System.out.print("\r" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576 + "  mo");
        return subResult;
    }

    /**
     * @return the number that codes the set of all pick ups
     */
    private long pickUpSetCode() {
        int n = deliveries.getSize();
        long code = 0;
        long a = 1;
        for (int k = 0; k < n; ++k) {
            code += a;
            a *= 4;
        }
        return code * 2;
    }

    /**
     * Creates the step between two vertices, using the results of Dijkstra's algorithms stored in pathsFromVertices
     *
     * @param departureIndex the departure of the step
     * @param arrivalIndex   the arrival of the step
     * @param time           the time when the step begin
     * @return the step
     */
    private Step makeStep(int departureIndex, int arrivalIndex, int time) {
        long departureId = idFromIndex(departureIndex);
        long arrivalId = idFromIndex(arrivalIndex);
        boolean arrivalType = arrivalIndex % 2 == 1;
        int arrivalDuration = durationFromIndex(arrivalIndex);
        Vertex arrival = new Vertex(arrivalId, arrivalType, arrivalDuration);

        Step step = new Step(arrival, deliveryIdFromIndex(arrivalIndex), time);
        if (departureId == arrivalId) {
            return step;
        }
        Section prevSection = pathsFromVertices.get(departureId).getPrevSection(arrivalId);
        while (prevSection != null) {
            step.addSection(prevSection);
            prevSection = pathsFromVertices.get(departureId).getPrevSection(prevSection.getOrigin());
        }
        double distance = pathsFromVertices.get(departureId).getLength(arrivalId);
        step.setArrivalDate(time + (int) (distance / CYCLIST_SPEED));
        return step;
    }

    private Round makeRound(ArrayList<Integer> path) {
        Round round = new Round(deliveries);
        int time = round.getStartDate();
        for (int i = 0; i < path.size() - 1; ++i) {
            Step step = makeStep(path.get(i), path.get(i + 1), time);
            round.addStep(step);
            time = step.getArrivalDate() + step.getDuration();
        }
        return round;
    }

    private ArrayList<Integer> makePath(SubResult subResult){
//        int n = deliveries.getSize();
        int n = truc;
        ArrayList<Integer> path = new ArrayList<>(2 * n + 1);
        path.add(0);
        long key = subResult.getNextKey();
        for(int i = 0; i < 2 * n; ++i){
            path.add((int)(key / powerSetSize));
            key = subResults.get(key).getNextKey();
        }
        return path;
    }

    /**
     * @return a round that minimize the total length
     */

    /*package-private*/ Round shortestRound() {
        var debut = System.nanoTime();
        SubResult subResult = resolveSubProblem(0, pickUpSetCode());
        var fin = System.nanoTime();
        System.out.print("TSP time : ");
        System.out.println((fin - debut) * 0.000000001);
        System.out.print("TSP length : ");
        System.out.println(subResult.getLength());
        ArrayList<Integer> path = makePath(subResult);
        return makeRound(path);
    }

    private Set<Integer> pickUpSet() {
        int n = deliveries.getSize();
        Set<Integer> pickUps = new TreeSet<>();
        for (int i = 1; i < 2 * n; i += 2) {
            pickUps.add(i);
        }
        return pickUps;
    }

    /*package-private*/ Round naiveRound() {
        int n = deliveries.getSize();
        ArrayList<Integer> path = new ArrayList<>(2 * n + 1);
        path.add(0);
        Set<Integer> candidates = pickUpSet();
        int last = 0;
        while (!candidates.isEmpty()) {
            int best = candidates.iterator().next();
            double bestDistance = lengths.get(last).get(best);
            for (int candidate : candidates) {
                if (lengths.get(last).get(candidate) < bestDistance) {
                    best = candidate;
                    bestDistance = lengths.get(last).get(candidate);
                }
            }
            candidates.remove(best);
            if (best % 2 == 1) {
                candidates.add(best + 1);
            }
            path.add(best);
            last = best;
        }
        return makeRound(path);
    }

    /**
     * The class that store an intermediary result
     */
    private static class SubResult {
        private double length;
        /** The length of the path */

        private long nextKey;

        SubResult(double length, long nextKey){
            this.length = length;
            this.nextKey = nextKey;
        }

        /** The indexes of the nodes of the path */

        /*package-private*/ double getLength() {
            return length;
        }

        /*package-private*/ long getNextKey() {
            return nextKey;
        }

        /*package-private*/ void setLength(double length) {
            this.length = length;
        }

        /*package-private*/ void setNextKey(long nextKey) {
            this.nextKey = nextKey;
        }
    }
}
