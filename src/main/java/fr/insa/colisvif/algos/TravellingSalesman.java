package fr.insa.colisvif.algos;

import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.util.Paire;

import java.util.*;

public class TravellingSalesman {
    static class Vertex{
        Long id;
        Boolean type; //0 if it is a pick up and 1 if it is a drop off

        public Long getId() { return id; }
        public boolean isPickUp() { return type; }
        public boolean isDropOff() { return !type; }

        Vertex(Long id, Boolean type){
            this.id = id;
            this.type = type;
        }
    }
    static class SubResult{
        LinkedList<Long> path;
        double length;

        LinkedList<Long> getPath() { return path; }
        double getLength() { return length; }
        void setLength(double length) { this.length = length; }
        void setPath(LinkedList<Long> path) { this.path = path; }

        SubResult(){
            path = new LinkedList<>();
            length = -1;
        }

        void add(Long l){
            path.add(l);
        }
    }

    private Long warehouseNodeId;
    private HashMap<Long, Long> dropOffs;
    private HashMap<Long, HashMap<Long, Double>> lengths;
    private HashMap<Paire<Long, Set<Long>>, SubResult> subresults;

    public TravellingSalesman(DeliveryMap deliveries, ShortestPaths shortestPaths){
        warehouseNodeId = deliveries.getWarehouseNodeId();
        subresults = new HashMap<>();
        dropOffs = new HashMap<>();
        for(Delivery delivery : deliveries.getDeliveryList()){
            dropOffs.put(delivery.getPickUpNodeId(), delivery.getDeliveryNodeId());
        }

        lengths = new HashMap<>();
        for(Long vertex1id : shortestPaths.getVertices()){
            HashMap<Long, Double> lengthsFromV1 = new HashMap<>();
            for(Long vertex2id : shortestPaths.getVertices()){
                lengthsFromV1.put(vertex2id, shortestPaths.getLength(vertex1id, vertex2id));
            }
            lengths.put(vertex1id, lengthsFromV1);
        }
    }

    private void update(Long start, Long id, SubResult subResult, SubResult candidate){
        double a = subResult.getLength();
        double b = candidate.getLength() + lengths.get(start).get(id);
        if(a == -1 || a > b){
            subResult.setLength(b);
            subResult.setPath(candidate.getPath());
        }
    }

    private void aux(Long start, Long id, SubResult subResult, TreeSet<Long> copy){
        copy.remove(id);
        if(dropOffs.containsKey(id)) {
            copy.add(dropOffs.get(id));
            SubResult candidate = subProblem(id, copy);
            update(start, id, subResult, candidate);
            copy.remove(dropOffs.get(id));
        }
        else{
            SubResult candidate = subProblem(id, copy);
            update(start, id, subResult, candidate);
        }
        copy.add(id);
    }

    private SubResult subProblem(Long start, Set<Long> vertices){
        Paire<Long, Set<Long>> key = new Paire<>(start, vertices);
        if(subresults.containsKey(key)) {
            return subresults.get(key);
        }
        if(vertices.size() == 1){
            Long l = vertices.iterator().next();
            SubResult subResult = new SubResult();
            subResult.add(l);
            subResult.add(start);
            subResult.setLength(lengths.get(start).get(l));
            subresults.put(key, subResult);
            return subResult;
        }

        SubResult subResult = new SubResult();
        TreeSet<Long> copy = new TreeSet<>(vertices);

        for(Long id : vertices){
            aux(start, id, subResult, copy);
        }
        subResult.setPath(new LinkedList<>(subResult.getPath()));
        subResult.add(start);
        subresults.put(key, subResult);
        return subResult;
    }

    public LinkedList<Vertex> shortestRound(){
        LinkedList<Long> L = subProblem(warehouseNodeId, lengths.keySet()).getPath();
        LinkedList<Vertex> V = new LinkedList<>();
        for( Long l : L){
            V.add(new Vertex(l, dropOffs.containsKey(l)));
        }
        return V;
    }
}
