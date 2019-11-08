package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CityMap {

    public static void main(String args[]) throws SAXException, IdError, ParserConfigurationException, IOException {
        File file = new File("/C:/Users/F\u00e9lix/Desktop/INSA/4IF/PLD agile/fichiersXML2019/grandPlan.xml");
        CityMap map = new CityMapFactory().createCityMapFromXMLFile(file);
        file = new File("/C:/Users/F\u00e9lix/Desktop/INSA/4IF/PLD agile/fichiersXML2019/demandeGrand7.xml");
        DeliveryMap deliveries = new DeliveryMapFactory().createDeliveryMapFromXML(file);

        Round round = map.naiveRound(deliveries);
    }

    private static final int LONG_MAX = 180;
    private static final int LAT_MIN = -90;
    private static final int speedInMS = (int)(15./3.6); //the speed of the cyclist in meters per second

    private double longMin;
    private double latMax;
    private Map<Long, Node> mapNode;
    private Map<String, List<Section>> mapSection;
    private HashMap<Long, PathsFromVertex> pathsFromVertices;

    public CityMap() {
        this.latMax = LAT_MIN;
        this.longMin = LONG_MAX;
        this.mapNode = new HashMap<>();
        this.mapSection = new HashMap<>();
    }

    public void createNode(long id, double latitude, double longitude) {
        Node newNode = new Node(id, latitude, longitude);
        this.mapNode.put(id, newNode);

        if(latitude > this.latMax){
            this.latMax = latitude;
        }

        if(longitude < this.longMin){
            this.longMin = longitude;
        }
    }

    public void createSection(double length, String roadName, long destination, long origin) throws IdError {
        Section newSection = new Section(length, roadName, destination, origin);

        if(this.mapSection.get(roadName) == null){
            List<Section> newSections = new ArrayList<>();
            newSections.add(newSection);
            this.mapSection.put(roadName, newSections);
        }
        else {
            this.mapSection.get(roadName).add(newSection);
        }

        this.mapNode.get(origin).addToSuccessors(newSection);
    }

    public void setLongMin(double longitude) {
        this.longMin = longitude;
    }

    public void setLatMax(double latitude) {
        this.latMax = latitude;
    }

    public double getLongMin() {
        return this.longMin;
    }

    public double getLatMax() {
        return this.latMax;
    }

    public Map<Long, Node> getMapNode() {
        return this.mapNode;
    }

    public Map<String, List<Section>> getMapSection() {
        return this.mapSection;
    }

    public Double getLength(Long start, Long finish){
        return pathsFromVertices.get(start).getLength(finish);
    }

    public Section getSection(Long start, Long finish){
        for(Section section : getMapNode().get(start).getSuccessors()){
            if(section.getDestination() == finish){
                return section;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        String result = "Nodes : \n";

        Set nodeKeys = this.mapNode.keySet();
        Iterator itN = nodeKeys.iterator();
        while (itN.hasNext()){
            Object nodeKey = itN.next();
            Node node = this.mapNode.get(nodeKey);
            result += node.toString();
        }

        result += "\nSections : \n";

        Set sectionKeys = this.mapSection.keySet();
        Iterator itS = sectionKeys.iterator();
        while (itS.hasNext()){
            Object sectionKey = itS.next();
            List<Section> sections = this.mapSection.get(sectionKey);
            for (Section s : sections)
                result += s.toString();
        }

        return result;
    }

    public void dijkstra(Long start){
        PathsFromVertex pathsFromStart = new PathsFromVertex();
        pathsFromStart.setLength(start, 0D);

        Comparator<Long> cmp = Comparator.comparingDouble(pathsFromStart::getLength);
        PriorityQueue<Long> Q = new PriorityQueue<Long>(cmp);
        Q.add(start);

        while(!Q.isEmpty()){
            Long node = Q.poll();
            double length = pathsFromStart.getLength(node);
            for(Section section : getMapNode().get(node).getSuccessors()){
                double destinationLength = pathsFromStart.getLength(section.getDestination());
                if(destinationLength == -1 || length + section.getLength() < destinationLength){
                    pathsFromStart.setLength(section.getDestination(),length + section.getLength());
                    pathsFromStart.setPrev(section.getDestination(), node);
                    Q.remove(section.getDestination());
                    Q.add(section.getDestination());
                }
            }
        }

        pathsFromVertices.put(start, pathsFromStart);
    }

    private int constructPath(Step step, long start, long finish, int time){
        if(start == finish){
            return time;
        }
        double length = 0D;
        while(start != finish){
            long prev = pathsFromVertices.get(start).prevVertices.get(finish);
            Section section = getSection(prev, finish);
            step.pushSection(section);
            finish = prev;
            length += section.getLength();
        }
        return time + (int)(length / speedInMS);
    }

    public Round shortestRound(DeliveryMap deliveries){
        int time = deliveries.getStartDateInSeconds();
        long lastId = deliveries.getWarehouseNodeId();
        Round round = new Round(deliveries);
        VerticesGraph G = new VerticesGraph(this, deliveries);
        List<Vertex> stopList = G.shortestRound();
        for(Vertex vertex : stopList){
            Step step = new Step(vertex);
            time = constructPath(step, lastId, vertex.getId(), time);
            step.setArrivalDateInSeconds(time);
            time += step.getDurationInSeconds();
            lastId = vertex.getId();
            round.pushStep(step);
        }
        return round;
    }

    public Round naiveRound(DeliveryMap deliveries){
        int time = deliveries.getStartDateInSeconds();
        long lastId = deliveries.getWarehouseNodeId();
        Round round = new Round(deliveries);
        VerticesGraph G = new VerticesGraph(this, deliveries);
        List<Vertex> stopList = G.naiveRound();
        for(Vertex vertex : stopList){
            Step step = new Step(vertex);
            time = constructPath(step, lastId, vertex.getId(), time);
            step.setArrivalDateInSeconds(time);
            time += step.getDurationInSeconds();
            lastId = vertex.getId();
            round.pushStep(step);
        }
        return round;
    }

    private static class PathsFromVertex{
        private HashMap<Long, Long> prevVertices;
        private HashMap<Long, Double> lengths;

        //No need to make a special constructor

        private void addPrev(Long id, Long prev){
            prevVertices.put(id, prev);
        }
        private void addLength(Long id, Double length){
            lengths.put(id, length);
        }
        private Long getPrev(Long id){
            return prevVertices.get(id);
        }
        private void setPrev(Long id, Long prev){
            prevVertices.put(id, prev);
        }
        private Double getLength(Long id){
            return lengths.get(id);
        }
        private void setLength(Long id, Double length){
            lengths.put(id, length);
        }
    }
}


