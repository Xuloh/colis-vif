package fr.insa.colisvif.model;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CityMap {
    private static final int LONG_MAX = 180;
    private static final int LAT_MIN = -90;

    private double longMin;
    private double latMax;
    private Map<Integer, Node> mapNode;
    private Map<String, List<Section>> mapSection;

    public CityMap() {
        this.latMax = LAT_MIN;
        this.longMin = LONG_MAX;
        this.mapNode = new HashMap<>();
        this.mapSection = new HashMap<>();
    }

    public void createNode(int id, double latitude, double longitude) {
        Node newNode = new Node(id, latitude, longitude);
        this.mapNode.put(id, newNode);

        if(latitude > this.latMax){
            this.latMax = latitude;
        }

        if(longitude < this.longMin){
            this.longMin = longitude;
        }
    }

    public void createSection(double length, String roadName, int destination, int origine) {
        Section newSection = new Section(length, roadName, destination, origine);

        if(this.mapSection.get(roadName).isEmpty()){
            List<Section> newSections = new ArrayList<>();
            newSections.add(newSection);
            this.mapSection.put(roadName, newSections);
        }
        else {
            this.mapSection.get(roadName).add(newSection);
        }

        this.mapNode.get(origine).addToSuccessors(newSection);
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

    public Map<Integer, Node> getMapNode() {
        return this.mapNode;
    }

    public Map<String, List<Section>> getMapSection() {
        return this.mapSection;
    }
}
