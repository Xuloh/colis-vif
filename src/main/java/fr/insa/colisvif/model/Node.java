package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Integer id;
    private Integer longitude;
    private Integer latitude;
    private List<Section> successors;

    public Node(Integer id, Integer longitude, Integer latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.successors = new ArrayList<>();
    }

    public Integer getLongitude() {
        return this.longitude;
    }

    public Integer getLatitude() {
        return this.latitude;
    }

    public Integer getId() {
        return this.id;
    }

    public List<Section> getSuccessors() {
        return this.successors;
    }

    public void addToSuccessors(Section section) {
        this.successors.add(section);
    }
}
