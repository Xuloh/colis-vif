package fr.insa.colisvif.model;

public class Warehouse {

    private long positionNodeId;
    private int startDateInSeconds;

    public Warehouse(long positionNodeId, int startDateInSeconds) {
        this.positionNodeId = positionNodeId;
        this.startDateInSeconds = startDateInSeconds;
    }

    public long getPositionNodeId() {
        return positionNodeId;
    }

    public void setPositionNodeId(long positionNodeId) {
        this.positionNodeId = positionNodeId;
    }

    public int getStartDateInSeconds() {
        return startDateInSeconds;
    }

    public void setStartDateInSeconds(int startDateInSeconds) {
        this.startDateInSeconds = startDateInSeconds;
    }
}
