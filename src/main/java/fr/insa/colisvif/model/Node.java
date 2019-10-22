package fr.insa.colisvif.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private long id;
    private double longitude;
    private double latitude;
    private List<Section> successors;

    public Node(long id, double latitude, double longitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.successors = new ArrayList<>();
        if (this.longitude > 180 || this.longitude < -180 || this.latitude < -90 || this.latitude > 90) {
            throw new IllegalArgumentException();
        }
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
