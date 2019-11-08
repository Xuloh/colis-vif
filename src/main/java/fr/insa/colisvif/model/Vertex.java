package fr.insa.colisvif.model;

import java.util.Objects;

public class Vertex implements Comparable<Vertex> {
    private long id;
    private boolean type; //0 if it is a pick up and 1 if it is a drop off
    private int durationInSeconds;

    public int getDurationInSeconds() {
        return durationInSeconds;
    }
    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
    public Long getId() { return id; }
    public boolean isPickUp() { return type; }
    public boolean isDropOff() { return !type; }

    Vertex(long id, boolean type, int durationInSeconds){
        this.id = id;
        this.type = type;
        this.durationInSeconds = durationInSeconds;
    }

    @Override
    public int compareTo(Vertex vertex) {
        if(this.id == vertex.id){
            if(this.type == vertex.type){
                if(this.durationInSeconds == vertex.durationInSeconds){
                    return 0;
                }
                return this.durationInSeconds - vertex.durationInSeconds;
            }
            if(this.type){
                return 1;
            }
            return -1;
        }
        return (int)(this.id - vertex.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id &&
                type == vertex.type &&
                durationInSeconds == vertex.durationInSeconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }


}
