package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityMap {
    private static final Integer LONG_MAX = 180;
    private static final Integer LAT_MIN = -90;

    private Integer longMin;
    private Integer latMax;
    private Map<Integer, Node> mapNode;
    private Map<String, List<Section>> mapSection;

    public CityMap() {
        this.latMax = LAT_MIN;
        this.longMin = LONG_MAX;
        this.mapNode = new HashMap<>();
        this.mapSection = new HashMap<>();
    }

    public void createNode(Integer id, Integer latitude, Integer longitude) {
        Node newNode = new Node(id, latitude, longitude);
        this.mapNode.put(id, newNode);

        if(latitude > this.latMax){
            this.latMax = latitude;
        }

        if(longitude < this.longMin){
            this.longMin = longitude;
        }
    }

    public void createSection(Integer length, String roadName, Integer destination, Integer origine) {
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

    public void setLongMin(Integer longitude) {
        this.longMin = longitude;
    }

    public void setLatMax(Integer latitude) {
        this.latMax = latitude;
    }

    public Integer getLongMin() {
        return this.longMin;
    }

    public Integer getLatMax() {
        return this.latMax;
    }

    public Map<Integer, Node> getMapNode() {
        return this.mapNode;
    }

    public Map<String, List<Section>> getMapSection() {
        return this.mapSection;
    }
}
