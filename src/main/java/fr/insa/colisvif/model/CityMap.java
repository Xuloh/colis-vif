package fr.insa.colisvif.model;

import java.util.*;

public class CityMap {
    private static final int LONG_MAX = 180;
    private static final int LAT_MIN = -90;

    private double longMin;
    private double latMax;
    private Map<Long, Node> mapNode;
    private Map<String, List<Section>> mapSection;

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

    public void createSection(double length, String roadName, long destination, long origin) {
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
}
