package fr.insa.colisvif.algos;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.model.*;
import fr.insa.colisvif.util.Paire;
//import fr.insa.colisvif.xml.DeliveryMapParserXML;
import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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
        int n = 11;
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

        TravellingSalesman TS = new TravellingSalesman(n, len);
        var debut = System.nanoTime();
        LinkedList<Vertex> L = TS.shortestRound();
        var fin = System.nanoTime();
        for(Vertex v : L){
            System.out.print(v.getId());
            System.out.print("  ");
        }
        System.out.println(" ");
        System.out.println((fin - debut)*0.000000001);
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

    public TravellingSalesman(int n, HashMap<Long, HashMap<Long, Double>> len){
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
            V.add(new Vertex(l, dropOffs.containsKey(l)));
        }
        return V;
    }
}
