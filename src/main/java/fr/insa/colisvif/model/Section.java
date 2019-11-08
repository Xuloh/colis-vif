package fr.insa.colisvif.model;

import java.util.Objects;

public class Section {
    private double length;

    private String roadName;

    private long destination;

    private long origin;

    public Section(double length, String roadName, long destination, long origin) {
        this.length = length;
        this.roadName = roadName;
        this.destination = destination;
        this.origin = origin;

        if (length <= 0) {
            throw new IllegalArgumentException();
        }
        //if(destination == origin) throw new IllegalArgumentException();
    }

    public double getLength() {
        return this.length;
    }

    public String getRoadName() {
        return this.roadName;
    }

    public long getDestination() {
        return this.destination;
    }

    public long getOrigin() {
        return this.origin;
    }

    @Override
    public String toString() {
        String result = "Length : " + length + " | Road Name : " + roadName + " | Destination : " + destination + " | Origin : " + origin + "\n";
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Double.compare(section.length, length) == 0
                && destination == section.destination
                && origin == section.origin
                && Objects.equals(roadName, section.roadName);
    }
}
