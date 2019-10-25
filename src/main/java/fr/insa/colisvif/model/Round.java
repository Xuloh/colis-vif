package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private List<Step> steps;
    private long warehouseNodeId;
    private int startDateInSeconds;


    Round(long warehouseNodeId, int startDateInSeconds){
        this.warehouseNodeId = warehouseNodeId;
        this.startDateInSeconds = startDateInSeconds;
        this.steps = new ArrayList<Step>();
    }

    public long getWarehouseNodeId() { return warehouseNodeId; }

    public void setWarehouseNodeId(long warehouseNodeId) { this.warehouseNodeId = warehouseNodeId; }

    public int getStartDateInSeconds() { return startDateInSeconds; }

    public void setStartDateInSeconds(int startDateInSeconds) { this.startDateInSeconds = startDateInSeconds; }

    public List<Step> getSteps() { return steps; }

    void addStep(Step step){

    }
}