package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private List<Step> steps;
    private long warehouseNodeId;
    private int startDateInSeconds;

    public long getWarehouseNodeId(){
        return warehouseNodeId;
    }
    public void setWarehouseNodeId(long warehouseNodeId){
        this.warehouseNodeId = warehouseNodeId;
    }
    public int getStartDateInSeconds(){
        return startDateInSeconds;
    }
    public void setStartDateInSeconds(int startDateInSeconds){
        this.startDateInSeconds = startDateInSeconds;
    }
    public List<Step> getSteps(){
        return steps;
    }

    public Round(long warehouseNodeId, int startDateInSeconds){
        steps = new ArrayList<>();
        this.warehouseNodeId = warehouseNodeId;
        this.startDateInSeconds = startDateInSeconds;
    }

    public void pushStep(Step step){
        steps.add(step);
    }
}