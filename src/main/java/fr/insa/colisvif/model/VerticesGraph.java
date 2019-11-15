package fr.insa.colisvif.model;

import java.util.*;

/*package-private*/ class VerticesGraph {

    // 2^(2n+1) where n is the number of deliveries, N is the number of
    //subsets of the vertices
    private long powerSetSize;

    // The deliveries that the cyclist need to deliver
    private DeliveryMap deliveries;

    // The results from the Dijkstra's algorithms
    private HashMap<Long, PathsFromVertex> pathsFromVertices;

    // The results from the Dijkstra's algorithms
    private ArrayList<ArrayList<Double>> lengths;

    // We will use dynamic programing, this is where we store the sub results
    private HashMap<Long, SubResult> subResults;

    /**
     * Let n be the number of deliveries we want to process
     * Creates a graph G with 2n+1 vertices : one for the warehouse and two for
     * each delivery (the pick up and the drop off)
     * The vertices are indexed from 0 to 2n : the warehouse is associated to 0
     * the pick up of the i-th delivery is associated to 2i+1
     * the drop off of this same delivery is associated to 2i+2
     *
     * @param deliveries        a delivery map we want to process
     * @param pathsFromVertices the results of Dijkstra's algorithms from all
     *                          nodes in deliveries
     */
    /*package-private*/
    VerticesGraph(DeliveryMap deliveries,
                  HashMap<Long, PathsFromVertex> pathsFromVertices) {
        int n = deliveries.getSize();
        powerSetSize = 0b1 << (2 * n + 1); //makes 2^(2n+1)
        this.deliveries = deliveries;
        this.pathsFromVertices = pathsFromVertices;

        //Computation of the edges's weights
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
        return delivery.getId();
    }

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

    private int durationFromIndex(int index) {
        Delivery delivery = deliveries.getDelivery((index - 1) / 2);
        if (index % 2 == 1) {
            return delivery.getPickUpDuration();
        }
        return delivery.getDropOffDuration();
    }

    /**
     * Calculates the key associated with a sub problem in the dynamic
     * programming process.This key will be used in the map subResults to
     * store the sub result of this sub problem
     * A sub problem is a couple (d, E) where d is a vertex of G and E a subset
     * of the vertices of G that does not contain d. The goal is to find the
     * shortest hamiltonian path of E u {k+1; k odd number in E} u {d} whose
     * first vertex is d
     *
     * @param start   the vertex d of the previous explanation
     * @param setCode a number that represents E, more precisely the sum of
     *                all 2^k for k in E
     * @return the key associated with (d, E)
     */
    private Long subProblemKey(int start, long setCode) {
        return setCode + powerSetSize * start;
    }

    /**
     * Returns the result of the sub problem (start, E) where S is the
     * subset coded by setCode
     *
     * @param start   the starting vertex of our path
     * @param setCode the subSet that need to be explored
     * @return the sub result of the sub problem (start, E)
     */
    private SubResult resolveSubProblem(int start, long setCode) throws InterruptedException {
        long key = subProblemKey(start, setCode);
        //this sub problem has been solved yet, we don't solve it again
        if (subResults.containsKey(key)) {
            return subResults.get(key);
        }
        if (setCode == 0) { //stop case, when E is the empty set
            SubResult subResult = new SubResult(0, 0);
            //the first 0 can be replaced by lengths.get(start).get(0) if we
            //want the cyclist to come back
            subResults.put(key, subResult);
            return subResult;
        }
        double bestLength = -1;
        long nextKey = 0;
        int n = deliveries.getSize();
        long a = 2; //will be 2^k, used to add and remove elements from the set
        long copy = setCode / 2; //will be setCode/2^k, used to know if k
        // belongs to the set
        for (int k = 1; k < 2 * n + 1; ++k) {
            if (Thread.interrupted()) {
                throw new InterruptedException("computation interrupted");
            }
            if ((copy & 1) == 1) { //k belongs to the set
                if ((k & 1) == 1) { //k is a pick up
                    //we remove the pick up from the set and add his associated
                    // drop off instead
                    SubResult candidate =
                            resolveSubProblem(k, setCode + a);
                    //setCode + a is in fact setCode - a + 2a,
                    // i.e.setCode - 2^k + 2^(k+1)
                    //that means we remove int k from the set
                    // and add k+1 instead
                    double candidateLength =
                            candidate.getLength() + lengths.get(start).get(k);
                    if (bestLength == -1 || bestLength > candidateLength) {
                        bestLength = candidateLength;
                        nextKey = subProblemKey(k, setCode + a);
                    }
                } else { //k is a drop off
                    //we remove the drop off from the set
                    SubResult candidate =
                            resolveSubProblem(k, setCode - a);
                    double candidateLength =
                            candidate.getLength() + lengths.get(start).get(k);
                    if (bestLength == -1 || bestLength > candidateLength) {
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

    private Step makeStep(int departureIndex, int arrivalIndex, int time) {
        long departureId = idFromIndex(departureIndex);
        long arrivalId = idFromIndex(arrivalIndex);
        boolean arrivalType =
                arrivalIndex % 2 == 1 ? Vertex.PICK_UP : Vertex.DROP_OFF;
        int arrivalDuration = durationFromIndex(arrivalIndex);
        Vertex arrival = new Vertex(arrivalId, arrivalType, arrivalDuration);
        arrival.setDeliveryId(this.deliveryIdFromIndex(arrivalIndex));

        Step step = new Step(arrival, deliveryIdFromIndex(arrivalIndex), time);
        if (departureId == arrivalId) {
            return step;
        }
        Section prevSection =
                pathsFromVertices.get(departureId).getPrevSection(arrivalId);
        while (prevSection != null) {
            step.addSection(prevSection);
            prevSection = pathsFromVertices.get(departureId)
                                           .getPrevSection(prevSection
                                           .getOrigin());
        }
        double distance =
                pathsFromVertices.get(departureId).getLength(arrivalId);
        time += (int) (distance / ModelConstants.CYCLIST_SPEED);
        step.setArrivalDate(time);
        step.setInitialArrivalDate();
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

    private ArrayList<Integer> makePath(SubResult subResult) {
        int n = deliveries.getSize();
        ArrayList<Integer> path = new ArrayList<>(2 * n + 1);
        path.add(0);
        long key = subResult.getNextKey();
        for (int i = 0; i < 2 * n; ++i) {
            path.add((int) (key / powerSetSize));
            key = subResults.get(key).getNextKey();
        }
        return path;
    }

    /**
     * @return a round that minimize the total length
     */
    /*package-private*/ Round shortestRound() throws InterruptedException {
        SubResult subResult = resolveSubProblem(0, pickUpSetCode());
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


    /**
     * @return a round where the cyclist always goes to the nearest step
     * among the ones he is allowed to go to
     * */
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

        private long nextKey;

        SubResult(double length, long nextKey) {
            this.length = length;
            this.nextKey = nextKey;
        }

        /*package-private*/ double getLength() {
            return length;
        }

        /*package-private*/ long getNextKey() {
            return nextKey;
        }
    }
}
