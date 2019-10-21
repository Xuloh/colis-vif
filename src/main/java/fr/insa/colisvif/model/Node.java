package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private double longitude;
    private double latitude;
    private List<Section> successors;

    public Node(int id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.successors = new ArrayList<>();
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public int getId() {
        return this.id;
    }

    public List<Section> getSuccessors() {
        return this.successors;
    }

    public void addToSuccessors(Section section) {
        this.successors.add(section);
    }
}
