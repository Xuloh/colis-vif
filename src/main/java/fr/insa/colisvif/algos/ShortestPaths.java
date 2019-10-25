package fr.insa.colisvif.algos;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Section;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

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
    private Map<Long, Map<Long, PrevAndLength>> paths;

    public Map<Long, Map<Long, PrevAndLength>> getPaths() { return paths; }

    public void dijkstra(CityMap map, Long start){
        Map<Long, PrevAndLength> pathsFromStart = new HashMap<>();
        for(Long key : map.getMapNode().keySet()){
            //System.out.println(key);
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

        paths.put(start, pathsFromStart);
    }

    public void dijkstra(CityMap map, DeliveryMap deliveries){
        dijkstra(map, deliveries.getWarehouseNodeId());
        for(Delivery delivery : deliveries.getDeliveryList()){
            dijkstra(map, delivery.getPickUpNodeId());
            dijkstra(map, delivery.getDeliveryNodeId());
        }
    }
}
