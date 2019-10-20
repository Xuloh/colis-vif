package fr.insa.colisvif.model;

public class Section {
    private Integer length;
    private String roadName;
    private Integer destination;
    private Integer origine;

    public Section(Integer length, String roadName, Integer destination, Integer origine) {
        this.length = length;
        this.roadName = roadName;
        this.destination = destination;
        this.origine = origine;
    }

    public Integer getLength() {
        return this.length;
    }

    public String getRoadName() {
        return this.roadName;
    }

    public Integer getDestination() {
        return this.destination;
    }

    public Integer getOrigine() {
        return this.origine;
    }
}
