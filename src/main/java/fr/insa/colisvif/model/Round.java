package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private List<Step> steps;
    private long warehouseNodeId;
    private int startDate;

    public long getWarehouseNodeId(){
        return warehouseNodeId;
    }
    public void setWarehouseNodeId(long warehouseNodeId){
        this.warehouseNodeId = warehouseNodeId;
    }
    public int getStartDate(){
        return startDate;
    }
    public void setStartDate(int startDate){
        this.startDate = startDate;
    }
    public List<Step> getSteps(){
        return steps;
    }

    public Round(DeliveryMap deliveries){
        steps = new ArrayList<>();
        warehouseNodeId = deliveries.getWarehouseNodeId();
        startDate = deliveries.getStartDateInSeconds();
    }

    public void addStep(Step step){
        steps.add(step);
    }
}