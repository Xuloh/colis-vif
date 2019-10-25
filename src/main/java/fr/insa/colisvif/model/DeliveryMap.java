package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;


public class DeliveryMap {

    private List<Delivery> deliveryRequests;
    private long warehouseNodeId;
    private int startDateInSeconds;


    public DeliveryMap() {
        this.deliveryRequests = new ArrayList<>();
    }

    public void createDelivery(long pickUpId, long deliveryId, int pickUpDuration, int deliveryDuration){
        Delivery newDelivery = new Delivery(pickUpId, deliveryId, pickUpDuration, deliveryDuration);
        this.deliveryRequests.add(newDelivery);
    }

    public void createWarehouse(long positionId, int startDateInSeconds){
        this.warehouseNodeId = positionId;
        this.startDateInSeconds = startDateInSeconds;
    }

    public List<Delivery> getDeliveryList() {
        return deliveryRequests;
    }

    public long getWarehouseNodeId() {
        return warehouseNodeId;
    }

    public void setWarehouseNodeId(long warehouseNodeId) {
        this.warehouseNodeId = warehouseNodeId;
    }

    public int getStartDateInSeconds() {
        return startDateInSeconds;
    }

    public void setStartDateInSeconds(int startDateInSeconds) {
        this.startDateInSeconds = startDateInSeconds;
    }
}
