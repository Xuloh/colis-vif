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

    public Round(DeliveryMap deliveries){
        steps = new ArrayList<>();
        warehouseNodeId = deliveries.getWarehouseNodeId();
        startDateInSeconds = deliveries.getStartDateInSeconds();
    }

    public void pushStep(Step step){
        steps.add(step);
    }
}