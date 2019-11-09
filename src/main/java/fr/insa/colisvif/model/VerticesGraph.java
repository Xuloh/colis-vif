package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/*package-private*/ class VerticesGraph {
    private static final int cyclistSpeed = (int)(15./3.6); /**The speed of the cyclist in meters per second */
    private long N; /**N=2^(2n+1) where n is the number of deliveries, N is the number of subsets of the vertices*/
    private DeliveryMap deliveries; /**The deliveries that the cyclist need to deliver*/
    private HashMap<Long, PathsFromVertex> pathsFromVertices; /**The results from the Dijkstra's algorithms*/
    private ArrayList<ArrayList<Double>> lengths; /**The weights of the arcs*/
    private HashMap<Long, SubResult> subResults; /**We will use dynamic programing, this is where we store the sub results*/

    /**
     * @param index a vertex in G
     * @return the id of the associated map node
     */
    private long idFromIndex(int index){
        if(index == 0){
            return deliveries.getWarehouseNodeId();
        }
        Delivery delivery = deliveries.getDelivery((index - 1) / 2);
        if(index%2 == 1){
            return delivery.getPickUpNodeId();
        }
        return delivery.getDropOffNodeId();
    }

    /**
     * @param index a vertex in G
     * @return the duration of the associated vertex
     */
    private int durationFromIndex(int index){
        Delivery delivery = deliveries.getDelivery((index - 1) / 2);
        if(index%2 == 1){
            return delivery.getPickUpDuration();
        }
        return delivery.getDropOffDuration();
    }

    /**
     * Let n be the number of deliveries we want to process
     * Creates a graph G with 2n+1 vertices : one for the warehouse and two for each delivery (the pick up and the drop off)
     * The vertices are indexed from 0 to 2n : the warehouse is associated to 0
     *                                         the pick up of the i-th delivery is associated to 2i+1
     *                                         the drop off of this same delivery is associated to 2i+2
     * @param deliveries a delivery map we want to process
     * @param pathsFromVertices the results of Dijkstra's algorithms from all nodes in deliveries
     */
    /*package-private*/ VerticesGraph(DeliveryMap deliveries, HashMap<Long, PathsFromVertex> pathsFromVertices){
        int n = deliveries.size();
        N = 0b1 << (2*n+1); //makes 2^(2n+1), thanks Pacôme
        this.deliveries = deliveries;
        this.pathsFromVertices = pathsFromVertices;

        lengths = new ArrayList<>(2*n+1);
        lengths.add(new ArrayList<>(2*n+1));
        for(int i=0; i<2*n+1; ++i){
            lengths.add(new ArrayList<>(2*n+1));
            long id1 = idFromIndex(i);
            for(int j=0; j<2*n+1; ++j){
                long id2 = idFromIndex(j);
                double len = pathsFromVertices.get(id1).getLength(id2);
                lengths.get(i).add(len);
            }
        }

        subResults = new HashMap<>();
    }

    /**
     * Calculates the key associated with a sub problem in the dynamic programming process
     * This key will be used in the map subResults to store the sub result of this sub problem
     * A sub problem is a couple (v, S) where v is a vertex of G and S a subset of the vertices of G that does not contain v
     * The goal is to find the shortest hamiltonian path of Su{v} whose first vertex is v
     * @param start the vertex v of the previous explanation
     * @param setCode a number that represents S, more precisely the sum of all 2^k for k in S
     * @return the key associated with (v, S)
     */
    private Long subProblemKey(int start, long setCode){
        return setCode + N * start;
    }

    /**
     * Returns the result of the sub problem (start, S) where S is the subset coded by setCode
     * @param start the starting vertex of our path
     * @param setCode the subSet that need to be explored
     * @return the sub result of the sub problem (start, s)
     */
    private SubResult resolveSubProblem(int start, long setCode){
        long key = subProblemKey(start, setCode);
        if(subResults.containsKey(key)){ //this sub problem has been solved yet, we don't solve it again
            return subResults.get(key);
        }
        if(setCode == 0){ //stop case, when S is the empty set
            SubResult subResult = new SubResult();
            subResult.setLength(0); //0 can be replaced by lengths.get(start).get(0) if we want the cyclist to come back
            subResult.addVertex(start);
            subResults.put(key, subResult);
            return subResult;
        }
        SubResult subResult = new SubResult();
        subResult.setLength(-1);
        int n = deliveries.size();
        long a = 2; //will be 2^k, used to add and remove elements from the set
        long copy = setCode / 2; //will be setCode/2^k, used to get the elements of the set
        for(int k = 1; k < 2 * n + 1; ++k){
            if(copy % 2 == 1){//k belongs to the set
                if(k % 2 == 1){//k is a pick up
                    //we remove the pick up from the set and add his associated drop off instead
                    SubResult candidate = resolveSubProblem(k, setCode + a);
                    //setCode+a is in fact setCode-a+2a, i.e.setCode-2^k+2^(k+1)
                    //that means we remove int k from the set and add k+1 instead
                    update(start, k, subResult, candidate);
                }
                else{//k is a drop off
                    //we remove the drop off from the set
                    SubResult candidate = resolveSubProblem(k, setCode - a);
                    update(start, k, subResult, candidate);
                }
            }
            a = 2 * a;
            copy = copy / 2;
        }
        subResult.setPath(new LinkedList<>(subResult.getPath()));
        subResult.addVertex(start);
        subResults.put(key, subResult);
        return subResult;
    }

    /**
     * Compares the sub results subResult and candidate, and updates subResult to candidate if candidate is better
     * Before the treatment, subResult's length is the length of the path starting from start and continuing with
     * subResult.getPath() . -1 stands for an "infinite" value
     * @param start the first vertex of the sub problem we are solving
     * @param next the fist vertex of candidate's path
     * @param subResult the best result we found so far
     * @param candidate a candidate that could be better
     */
    private void update(int start, int next, SubResult subResult, SubResult candidate){
        double a = subResult.getLength();
        double b = candidate.getLength() + lengths.get(start).get(next);
        if(a == -1 || a > b) {
            subResult.setLength(b);
            subResult.setPath(candidate.getPath());
        }
    }

    /**
     * @return the number that codes the set of all pick ups
     */
    private long pickUpSetCode(){
        int n = deliveries.size();
        long code = 0;
        long a = 1;
        for(int k = 0; k < n; ++k){
            code += a;
            a *= 4;
        }
        return code * 2;
    }

    /**
     * Creates the step between two vertices, using the results of Dijkstra's algorithms stored in pathsFromVertices
     * @param departureIndex the departure of the step
     * @param arrivalIndex the arrival of the step
     * @param time the time when the step begin
     * @return the step
     */
    private Step makeStep(int departureIndex, int arrivalIndex, int time){
        long departureId = idFromIndex(departureIndex);
        long arrivalId = idFromIndex(arrivalIndex);
        boolean arrivalType = arrivalIndex % 2 == 1;
        int arrivalDuration = durationFromIndex(arrivalIndex);
        Vertex arrival = new Vertex(arrivalId, arrivalType, arrivalDuration);

        Step step = new Step(arrival);
        if(departureId == arrivalId){
            step.setArrivalDate(time);
            return step;
        }
        Section prevSection = pathsFromVertices.get(departureId).getPrevSection(arrivalId);
        while(prevSection.getOrigin() != arrivalId){
            step.addSection(prevSection);
        }
        double distance = pathsFromVertices.get(departureId).getLength(arrivalId);
        step.setArrivalDate(time + (int)(distance / cyclistSpeed));
        return step;
    }

    /**
     * @return a round that minimize the total length
     */
    /*package-private*/ Round shortestRound(){
        SubResult subResult = resolveSubProblem(0, pickUpSetCode());
        ArrayList<Integer> path = new ArrayList<>(subResult.getPath());
        Round round = new Round(deliveries);
        int time = round.getStartDate();
        for(int i = 0; i < path.size() - 1; ++i){
            Step step = makeStep(path.get(i), path.get(i + 1), time);
            round.addStep(step);
            time = step.getArrivalDate() + step.getDuration();
        }
        return round;
    }

    /**
     * The class that store an intermediary result
     */
    private static class SubResult {
        private double length; /**The length of the path*/
        private LinkedList<Integer> path;/**The indexes of the nodes of the path*/

        /*package-private*/ SubResult(){
            length = 0;
            path = new LinkedList<>();
        }

        /*package-private*/ double getLength() {
            return length;
        }
        /*package-private*/ LinkedList<Integer> getPath() {
            return path;
        }
        /*package-private*/ void setLength(double length) {
            this.length = length;
        }
        /*package-private*/ void setPath(LinkedList<Integer> path) {
            this.path = path;
        }

        /**
         * Add a vertex index at the beginning of the path
         * @param vertexIndex the index added
         */
        /*package-private*/ void addVertex(int vertexIndex){
            path.addFirst(vertexIndex);
        }
    }
}
