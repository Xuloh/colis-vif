package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;

import java.util.*;

public class CityMap {
    private static final int LNG_MAX = 180;

    private static final int LAT_MIN = -90;

    private double lngMin;

    private double lngMax;

    private double latMin;

    private double latMax;

    private Map<Long, Node> mapNode;

    private Map<String, List<Section>> mapSection;

    public CityMap() {
        this.latMax = LAT_MIN;
        this.lngMin = LNG_MAX;
        this.mapNode = new HashMap<>();
        this.mapSection = new HashMap<>();
    }

    public void createNode(long id, double latitude, double longitude) {
        Node newNode = new Node(id, latitude, longitude);
        this.mapNode.put(id, newNode);

        if (latitude > this.latMax) {
            this.latMax = latitude;
        }

        if (longitude < this.lngMin) {
            this.lngMin = longitude;
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

    public void setLngMin(double longitude) {
        this.lngMin = longitude;
    }

    public void setLatMax(double latitude) {
        this.latMax = latitude;
    }

    public double getLngMin() {
        return this.lngMin;
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
}
