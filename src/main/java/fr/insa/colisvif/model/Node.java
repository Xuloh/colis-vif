package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private long id;
    private double longitude;
    private double latitude;
    private List<Section> successors;

    public Node(long id, double longitude, double latitude) {
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

    public long getId() {
        return this.id;
    }

    public List<Section> getSuccessors() {
        return this.successors;
    }

    public void addToSuccessors(Section section) {
        this.successors.add(section);
    }

    @Override
    public String toString() {
        String result = "ID : " + id + " | Latitude :  " + latitude + " | Longitude : " + longitude + "\n";
        return result;
    }
}
