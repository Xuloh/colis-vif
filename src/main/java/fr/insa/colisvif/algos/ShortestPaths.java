package fr.insa.colisvif.algos;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Section;

import java.util.*;

public class ShortestPaths {
    static class PrevAndLength {
        private Long prev;
        private double length;

        PrevAndLength(){
            prev = null;
            length = -1;
        }

        Long getPrev() { return prev; }
        double getLength() { return length; }
        void setPrev(Long prev) { this.prev = prev; }
        void setLength(double length) { this.length = length; }
    }

    private Long warehouseNodeId;
    private HashMap<Long, HashMap<Long, PrevAndLength>> pathsFromVertices;

    public ShortestPaths(CityMap map, DeliveryMap deliveries){
        warehouseNodeId = deliveries.getWarehouseNodeId();
        pathsFromVertices = new HashMap<>();

        dijkstra(map, deliveries.getWarehouseNodeId());
        for(Delivery delivery : deliveries.getDeliveryList()){
            dijkstra(map, delivery.getPickUpNodeId());
            dijkstra(map, delivery.getDeliveryNodeId());
        }
    }

    public Long getWarehouseNodeId() { return warehouseNodeId; }
    //public HashMap<Long, HashMap<Long, PrevAndLength>> getPathsFromVertices() { return pathsFromVertices; }
    public double getLength(Long vertex1Id, Long vertex2Id){
        return pathsFromVertices.get(vertex1Id).get(vertex2Id).getLength();
    }
    public Set<Long> getVertices() { return pathsFromVertices.keySet(); }

    private void dijkstra(CityMap map, Long start){
        HashMap<Long, PrevAndLength> pathsFromStart = new HashMap<>();
        for(Long key : map.getMapNode().keySet()){
            pathsFromStart.put(key, new PrevAndLength());
        }
        pathsFromStart.get(start).setLength(0);

        Comparator<Long> cmp = Comparator.comparingDouble(l -> pathsFromStart.get(l).getLength());
        PriorityQueue<Long> Q = new PriorityQueue<Long>(cmp);
        Q.add(start);

        while(!Q.isEmpty()){
            Long node = Q.poll();
            double length = pathsFromStart.get(node).getLength();
            for(Section section : map.getMapNode().get(node).getSuccessors()){
                double destinationLength = pathsFromStart.get(section.getDestination()).getLength();
                if(destinationLength == -1 || length + section.getLength() < destinationLength){
                    pathsFromStart.get(section.getDestination()).setLength(length + section.getLength());
                    pathsFromStart.get(section.getDestination()).setPrev(node);
                    Q.remove(section.getDestination());
                    Q.add(section.getDestination());
                }
            }
        }
        pathsFromVertices.put(start, pathsFromStart);
    }
}
