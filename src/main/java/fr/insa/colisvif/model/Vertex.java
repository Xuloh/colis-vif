package fr.insa.colisvif.model;

import java.util.Objects;

public class Vertex implements Comparable<Vertex> {

    public static final boolean DROP_OFF = false;

    public static final boolean PICK_UP = false;

    private long nodeId;

    private boolean type; //0 if it is a pick up and 1 if it is a drop off

    private int duration;

    public Vertex(long nodeId, boolean type, int duration) {
        this.nodeId = nodeId;
        this.type = type;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int durationInSeconds) {
        this.duration = durationInSeconds;
    }

    public long getNodeId() {
        return nodeId;
    }

    public boolean isPickUp() {
        return type;
    }

    @Override
    public int compareTo(Vertex vertex) {
        if (this.nodeId == vertex.nodeId) {
            if (this.type == vertex.type) {
                if (this.duration == vertex.duration) {
                    return 0;
                }
                return this.duration - vertex.duration;
            }
            if (this.type) {
                return 1;
            }
            return -1;
        }
        return (int) (this.nodeId - vertex.nodeId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return nodeId == vertex.nodeId
                && type == vertex.type
                && duration == vertex.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, type);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + nodeId +
                ", type=" + type +
                ", durationInSeconds=" + duration +
                '}';
    }
}
