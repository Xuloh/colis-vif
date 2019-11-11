package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CityMap {

    private static final int LNG_MIN = -180;

    private static final int LNG_MAX = 180;

    private static final int LAT_MIN = -90;

    private static final int LAT_MAX = 90;

    private double lngMin;

    private double lngMax;

    private double latMin;

    private double latMax;

    private Map<Long, Node> mapNode;

    private Map<String, List<Section>> mapSection;
    private HashMap<Long, PathsFromVertex> pathsFromVertices;

    public CityMap() {
        this.latMin = LAT_MAX;
        this.latMax = LAT_MIN;
        this.lngMin = LNG_MAX;
        this.lngMax = LNG_MIN;
        this.mapNode = new HashMap<>();
        this.mapSection = new HashMap<>();
        this.pathsFromVertices = new HashMap<>();
    }

    public void createNode(long id, double latitude, double longitude) {
        Node newNode = new Node(id, latitude, longitude);
        this.mapNode.put(id, newNode);

        if (latitude < this.latMin) {
            this.latMin = latitude;
        }

        if (latitude > this.latMax) {
            this.latMax = latitude;
        }

        if (longitude < this.lngMin) {
            this.lngMin = longitude;
        }

        if (longitude > this.lngMax) {
            this.lngMax = longitude;
        }
    }

    public void createSection(double length, String roadName, long destination, long origin) throws IdError {
        Section newSection = new Section(length, roadName, destination, origin);

        if (this.mapSection.get(roadName) == null) {
            List<Section> newSections = new ArrayList<>();
            newSections.add(newSection);
            this.mapSection.put(roadName, newSections);
        } else {
            this.mapSection.get(roadName).add(newSection);
        }

        this.mapNode.get(origin).addToSuccessors(newSection);
    }

    public double getLngMin() {
        return this.lngMin;
    }

    public double getLngMax() {
        return this.lngMax;
    }

    public double getLatMin() {
        return this.latMin;
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
        StringBuilder result = new StringBuilder("Nodes : \n");

        Set<Long> nodeKeys = this.mapNode.keySet();
        for (Long nodeKey : nodeKeys) {
            Node node = this.mapNode.get(nodeKey);
            result.append(node.toString());
        }

        result.append("\nSections : \n");

        Set<String> sectionKeys = this.mapSection.keySet();
        for (String sectionKey : sectionKeys) {
            List<Section> sections = this.mapSection.get(sectionKey);
            for (Section s : sections) {
                result.append(s.toString());
            }
        }

        return result.toString();
    }


    public void dijkstra(Long start){
        PathsFromVertex pathsFromStart = new PathsFromVertex();
        pathsFromStart.setLength(start, 0D);
        pathsFromStart.setPrev(start, null);

        Comparator<Long> cmp = Comparator.comparingDouble(pathsFromStart::getLength);
        PriorityQueue<Long> Q = new PriorityQueue<Long>(cmp);
        Q.add(start);

        while(!Q.isEmpty()){
            Long node = Q.poll();
            double length = pathsFromStart.getLength(node);
            for(Section section : getMapNode().get(node).getSuccessors()){
                long next = section.getDestination();
                double destinationLength = pathsFromStart.getLength(next);
                if(destinationLength == -1 || length + section.getLength() < destinationLength){
                    pathsFromStart.setLength(next,length + section.getLength());
                    pathsFromStart.setPrev(next, section);
                    Q.remove(next);
                    Q.add(next);
                }
            }
        }

        pathsFromVertices.put(start, pathsFromStart);
    }

    public Round shortestRound(DeliveryMap deliveries){
        var debut = System.nanoTime();
        dijkstra(deliveries.getWarehouseNodeId());
        for(Delivery delivery : deliveries.getDeliveryList()){
            dijkstra(delivery.getPickUpNodeId());
            dijkstra(delivery.getDropOffNodeId());
        }
        var fin = System.nanoTime();
        System.out.print("Dijkstra time : ");
        System.out.println((fin - debut)*0.000000001);
        VerticesGraph G = new VerticesGraph(deliveries, pathsFromVertices);
        return G.shortestRound();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityMap cityMap = (CityMap) o;
        return Double.compare(cityMap.lngMin, lngMin) == 0 &&
                Double.compare(cityMap.lngMax, lngMax) == 0 &&
                Double.compare(cityMap.latMin, latMin) == 0 &&
                Double.compare(cityMap.latMax, latMax) == 0 &&
                Objects.equals(mapNode, cityMap.mapNode) &&
                Objects.equals(mapSection, cityMap.mapSection);
    }

}


