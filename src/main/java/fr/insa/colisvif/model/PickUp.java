package fr.insa.colisvif.model;

public class PickUp {
    private long nodeId;

    private int duration;

    public PickUp(long nodeId, int duration) {
        this.nodeId = nodeId;
        this.duration = duration;
    }

    public long getNodeId() {
        return nodeId;
    }

    public int getDuration() {
        return duration;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }
}
