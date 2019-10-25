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

    public ShortestPaths(CityMap map, DeliveryMap deliveries){
        warehouseNodeId = deliveries.getWarehouseNodeId();
        pathsFromVertices = new HashMap<>();
        lengths = new HashMap<>();

        dijkstra(map, deliveries.getWarehouseNodeId());
        lengths.put(warehouseNodeId, new HashMap<>());

        for(Delivery delivery : deliveries.getDeliveryList()){
            Long pickUp = delivery.getPickUpNodeId();
            Long dropOff = delivery.getDeliveryNodeId();
            lengths.put(pickUp, new HashMap<>());
            lengths.put(dropOff, new HashMap<>());
            lengths.get(warehouseNodeId).put(pickUp, pathsFromVertices.get(warehouseNodeId).get(pickUp).getLength());
            lengths.get(warehouseNodeId).put(dropOff, pathsFromVertices.get(warehouseNodeId).get(dropOff).getLength());
            dijkstra(map, pickUp);
            dijkstra(map, dropOff);
            lengths.get(pickUp).put(warehouseNodeId, pathsFromVertices.get(pickUp).get(warehouseNodeId).getLength());
            lengths.get(dropOff).put(warehouseNodeId, pathsFromVertices.get(dropOff).get(warehouseNodeId).getLength());
        }
        for(Delivery delivery1 : deliveries.getDeliveryList()){
            Long pickUp1 = delivery1.getPickUpNodeId();
            Long dropOff1 = delivery1.getDeliveryNodeId();
            for(Delivery delivery2 : deliveries.getDeliveryList()){
                Long pickUp2 = delivery2.getPickUpNodeId();
                Long dropOff2 = delivery2.getDeliveryNodeId();
                lengths.get(pickUp1).put(pickUp2, pathsFromVertices.get(pickUp1).get(pickUp2).getLength());
                lengths.get(pickUp1).put(dropOff1, pathsFromVertices.get(pickUp1).get(dropOff1).getLength());
                lengths.get(pickUp1).put(dropOff2, pathsFromVertices.get(pickUp1).get(dropOff2).getLength());
                lengths.get(pickUp2).put(pickUp1, pathsFromVertices.get(pickUp2).get(pickUp1).getLength());
                lengths.get(pickUp2).put(dropOff1, pathsFromVertices.get(pickUp2).get(dropOff1).getLength());
                lengths.get(pickUp2).put(dropOff2, pathsFromVertices.get(pickUp2).get(dropOff2).getLength());
                lengths.get(dropOff1).put(pickUp1, pathsFromVertices.get(dropOff1).get(pickUp1).getLength());
                lengths.get(dropOff1).put(pickUp2, pathsFromVertices.get(dropOff1).get(pickUp2).getLength());
                lengths.get(dropOff1).put(dropOff2, pathsFromVertices.get(dropOff1).get(dropOff2).getLength());
                lengths.get(dropOff2).put(pickUp1, pathsFromVertices.get(dropOff2).get(pickUp1).getLength());
                lengths.get(dropOff2).put(pickUp2, pathsFromVertices.get(dropOff2).get(pickUp2).getLength());
                lengths.get(dropOff2).put(dropOff1, pathsFromVertices.get(dropOff2).get(dropOff1).getLength());
            }
        }
    }

    private Long warehouseNodeId;
    private HashMap<Long, HashMap<Long, PrevAndLength>> pathsFromVertices;
    private HashMap<Long, HashMap<Long, Double>> lengths; // à garder ?
    private List<Long> shortestHamiltonianPath;

    public HashMap<Long, HashMap<Long, PrevAndLength>> getPathsFromVertices() { return pathsFromVertices; }
    public List<Long> getShortestHamiltonianPath() { return shortestHamiltonianPath; }

    public void dijkstra(CityMap map, Long start){
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
