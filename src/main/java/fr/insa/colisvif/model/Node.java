package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;

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

    public void addToSuccessors(Section section) throws IdError{
        if (section.getOrigin() == this.id)
            this.successors.add(section);
        else
            throw new IdError("Origine du tronçon différente de ce noeud");
    }

    @Override
    public String toString() {
        String result = "ID : " + id + " | Latitude : " + latitude + " | Longitude : " + longitude + "\n";
        return result;
    }
}
