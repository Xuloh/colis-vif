package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.util.Paire;
import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class VerticesGraph {
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
        void clearPath(){
            path.clear();
        }
    }

    public static void main(String args[]) throws SAXException, IdError, ParserConfigurationException, IOException {
        /*
        File file = new File("/C:/Users/F\u00e9lix/Desktop/INSA/4IF/PLD agile/fichiersXML2019/Tests/map1.xml");
        CityMap map = new CityMapFactory().createCityMapFromXMLFile(file);
        file = new File("/C:/Users/F\u00e9lix/Desktop/INSA/4IF/PLD agile/fichiersXML2019/Tests/delivery1.xml");
        DeliveryMap deliveries = new DeliveryMapFactory(new DeliveryMapParserXML()).createDeliveryMapFromXML(file);


        for(Long node : map.getMapNode().keySet()){
            System.out.println(node);
            for(Section section : map.getMapNode().get(node).getSuccessors()){
                System.out.print("->");
                System.out.print(section.getDestination());
                System.out.print("   ");
                System.out.println(section.getLength());
            }
            System.out.println("");
        }


        ShortestPaths SP = new ShortestPaths(map, deliveries);
        TravellingSalesman TSP = new TravellingSalesman(deliveries, SP);
        LinkedList<Vertex> truc = TSP.shortestRound();
        for(Vertex machin : truc){
            System.out.println(machin.id);
        }
*/
        int n = 9;
        int k = 2*n+1;
        HashMap<Long, HashMap<Long, Double>> len = new HashMap<>();
        for(Long i = 0L; i < k; ++i){
            len.put(i, new HashMap<>());
        }

        for(Long i = 0L; i<k; ++i){
            for(Long j=0L; j<k; ++j){
                len.get(i).put(j, Math.abs(Math.cos(i) + Math.sin(j)) + 0.001);
            }
        }

        /*
        for(Long i = 0L; i < k * k; ++i){
            len.get(i / k).put(i % k, (double) (i + 1L));
        }

        for(Long i = 0L; i < k; ++i){
            for(Long j = 0L; j < k; ++j){
                System.out.print(i);
                System.out.print(" -> ");
                System.out.print(j);
                System.out.print("   ");
                System.out.println(len.get(i).get(j));
            }
        }
*/

        VerticesGraph TS = new VerticesGraph(n, len);
        var debut = System.nanoTime();
        LinkedList<Vertex> L = TS.shortestRound();
        var fin = System.nanoTime();
        for(Vertex v : L){
            System.out.print(v.getId());
            System.out.print("  ");
        }
        System.out.println(" ");
        System.out.println((fin - debut)*0.000000001);


//        var debut = System.nanoTime();
//        var L = TS.naiveRound();
//        var fin = System.nanoTime();
//        for(Vertex v : L){
//            System.out.print(v.getId());
//            System.out.print("  ");
//        }
//        System.out.println(" ");
//        System.out.println((fin - debut)*0.000000001);
    }

    private Long warehouseNodeId;
    private HashMap<Long, Long> dropOffs;
    private HashMap<Long, HashMap<Long, Double>> lengths;
    private HashMap<Paire<Long, Set<Long>>, SubResult> subresults;

    public VerticesGraph(CityMap map, DeliveryMap deliveries){
        warehouseNodeId = deliveries.getWarehouseNodeId();
        subresults = new HashMap<>();
        dropOffs = new HashMap<>();
        for(Delivery delivery : deliveries.getDeliveryList()){
            dropOffs.put(delivery.getPickUpNodeId(), delivery.getDeliveryNodeId());
        }

        lengths = new HashMap<>();
        lengths.put(warehouseNodeId, new HashMap<>());
        map.dijkstra(warehouseNodeId);
        for(Delivery delivery1 : deliveries.getDeliveryList()){
            Long pickUp1 = delivery1.getPickUpNodeId();
            Long dropOff1 = delivery1.getDeliveryNodeId();
            lengths.put(pickUp1, new HashMap<>());
            lengths.put(dropOff1, new HashMap<>());
            map.dijkstra(pickUp1);
            map.dijkstra(dropOff1);
            for(Delivery delivery2 : deliveries.getDeliveryList()){
                Long pickUp2 = delivery2.getPickUpNodeId();
                Long dropOff2 = delivery2.getDeliveryNodeId();
                lengths.get(pickUp1).put(pickUp2, map.getLength(pickUp1, pickUp2));
                lengths.get(pickUp1).put(dropOff2, map.getLength(pickUp1, dropOff2));
                lengths.get(dropOff1).put(pickUp2, map.getLength(dropOff1, pickUp2));
                lengths.get(dropOff1).put(dropOff2, map.getLength(dropOff1, dropOff2));
            }
            lengths.get(warehouseNodeId).put(pickUp1, map.getLength(warehouseNodeId, pickUp1));
            lengths.get(warehouseNodeId).put(dropOff1, map.getLength(warehouseNodeId, dropOff1));
        }
    }

    public VerticesGraph(int n, HashMap<Long, HashMap<Long, Double>> len){
        warehouseNodeId = 0L;
        subresults = new HashMap<>();
        dropOffs = new HashMap<>();
        for(long i = 1; i < 2 * n; i+=2){
            dropOffs.put(i, i+1);
        }
        lengths = len;
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
        Paire<Long, Set<Long>> key = new Paire<>(start, new TreeSet<>(vertices));

        if(subresults.containsKey(key)) {
            return subresults.get(key);
        }
        if(vertices.isEmpty()){
            SubResult subResult = new SubResult();
            subResult.add(start);
            subResult.setLength(0);
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
        LinkedList<Long> L = subProblem(warehouseNodeId, dropOffs.keySet()).getPath();
        LinkedList<Vertex> V = new LinkedList<>();
        for( Long l : L){
            V.addFirst(new Vertex(l, dropOffs.containsKey(l)));
        }
        return V;
    }

    public LinkedList<Vertex> naiveRound(){
        LinkedList<Vertex> V = new LinkedList<>();
        Set<Long> S = new TreeSet<>(dropOffs.keySet());

        V.add(new Vertex(warehouseNodeId, dropOffs.containsKey(warehouseNodeId)));
        Long last = warehouseNodeId;
        while(!S.isEmpty()){
            Long candidate = S.iterator().next();
            Double distance = lengths.get(last).get(candidate);
            for(Long l : S){
                if(lengths.get(last).get(l) < distance){
                    candidate = l;
                    distance = lengths.get(last).get(l);
                }
            }
            V.add(new Vertex(candidate, dropOffs.containsKey(candidate)));
            if(dropOffs.containsKey(candidate)){
                S.add(dropOffs.get(candidate));
            }
            S.remove(candidate);
            last = candidate;
        }

        return V;
    }
}
