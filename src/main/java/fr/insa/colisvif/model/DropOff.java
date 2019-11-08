package fr.insa.colisvif.model;

public class DropOff {
    private long nodeId;
    private int duration;

    public DropOff(long nodeId, int duration) {
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
