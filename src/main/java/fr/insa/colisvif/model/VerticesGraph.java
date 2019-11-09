package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.util.Paire;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class VerticesGraph {
    static class SubResult{
        LinkedList<Vertex> path;
        double length;

        LinkedList<Vertex> getPath() { return path; }
        double getLength() { return length; }
        void setLength(double length) { this.length = length; }
        void setPath(LinkedList<Vertex> path) { this.path = path; }

        SubResult(){
            path = new LinkedList<>();
            length = -1;
        }

        void add(Vertex vertex){
            path.add(vertex);
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

//        var debut = System.nanoTime();
//        LinkedList<Vertex> L = TS.shortestRound();
//        var fin = System.nanoTime();
//        for(Vertex v : L){
//            System.out.print(v.getId());
//            System.out.print("  ");
//        }
//        System.out.println(" ");
//        System.out.println((fin - debut)*0.000000001);


        /*
        var debut = System.nanoTime();
        var L = TS.naiveRound();
        var fin = System.nanoTime();
        for(Vertex v : L){
            System.out.print(v.getId());
            System.out.print("  ");
        }
        System.out.println(" ");
        System.out.println((fin - debut)*0.000000001);*/
    }

    private Long warehouseNodeId;
    private HashMap<Long, Long> dropOffs; // TODO remplacer ça par une liste de dropoffs associés
    private HashMap<Long, HashMap<Long, Double>> lengths;
    private HashMap<Paire<Vertex, Set<Vertex>>, SubResult> subresults;

    public VerticesGraph(CityMap map, DeliveryMap deliveries){
        warehouseNodeId = deliveries.getWarehouseNodeId();
        subresults = new HashMap<>();
        dropOffs = new HashMap<>();
        for(Delivery delivery : deliveries.getDeliveryList()){
            dropOffs.put(delivery.getPickUpNodeId(), delivery.getDropOffNodeId());
        }

        lengths = new HashMap<>();
        lengths.put(warehouseNodeId, new HashMap<>());
        map.dijkstra(warehouseNodeId);
        for(Delivery delivery1 : deliveries.getDeliveryList()){
            Long pickUp1 = delivery1.getPickUpNodeId();
            Long dropOff1 = delivery1.getDropOffNodeId();
            lengths.put(pickUp1, new HashMap<>());
            lengths.put(dropOff1, new HashMap<>());
            map.dijkstra(pickUp1);
            map.dijkstra(dropOff1);
            for(Delivery delivery2 : deliveries.getDeliveryList()){
                Long pickUp2 = delivery2.getPickUpNodeId();
                Long dropOff2 = delivery2.getDropOffNodeId();
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

    /*private void aux(Vertex start, Vertex vertex, SubResult subResult, TreeSet<Vertex> copy){
        copy.remove(vertex);
        Long id = vertex.getId();
        if(vertex.isPickUp()) {
            Vertex next = new Vertex(dropOffs.get(id), true);
            copy.add(next);
            SubResult candidate = subProblem(vertex, copy);
            update(start.getId(), id, subResult, candidate);
            copy.remove(next);
        }
        else{
            SubResult candidate = subProblem(vertex, copy);
            update(start.getId(), id, subResult, candidate);
        }
        copy.add(vertex);
    }*/

    /*private SubResult subProblem(Vertex start, Set<Vertex> vertices){
        Paire<Vertex, Set<Vertex>> key = new Paire<>(start, new TreeSet<>(vertices));

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
        TreeSet<Vertex> copy = new TreeSet<>(vertices);

        for(Vertex vertex : vertices){
            aux(start, vertex, subResult, copy);
        }
        subResult.setPath(new LinkedList<>(subResult.getPath()));
        subResult.add(start);

        subresults.put(key, subResult);
        return subResult;
    }*/

    /*
    public LinkedList<Vertex> shortestRound(){
        Vertex start = new Vertex(warehouseNodeId, true);
        TreeSet<Vertex> vertices = new TreeSet<>();
        for(Long l : dropOffs.keySet()){
            vertices.add(new Vertex(l, false));
        }
        LinkedList<Vertex> stops = subProblem(start, vertices).getPath();
        stops.removeFirst();
        return stops;
    }*/

    /*
    public LinkedList<Vertex> naiveRound(){
        LinkedList<Vertex> stopList = new LinkedList<>();
        Set<Long> S = new TreeSet<>(dropOffs.keySet());

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
            stopList.add(new Vertex(candidate, dropOffs.containsKey(candidate)));
            if(dropOffs.containsKey(candidate)){
                S.add(dropOffs.get(candidate));
            }
            S.remove(candidate);
            last = candidate;
        }

        return stopList;
    }*/
}
