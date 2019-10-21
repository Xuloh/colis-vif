package fr.insa.colisvif.model;

public class Section {
    private double length;
    private String roadName;
    private int destination;
    private int origine;

    public Section(double length, String roadName, int destination, int origine) {
        this.length = length;
        this.roadName = roadName;
        this.destination = destination;
        this.origine = origine;
    }

    public double getLength() {
        return this.length;
    }

    public String getRoadName() {
        return this.roadName;
    }

    public int getDestination() {
        return this.destination;
    }

    public int getOrigine() {
        return this.origine;
    }
}
