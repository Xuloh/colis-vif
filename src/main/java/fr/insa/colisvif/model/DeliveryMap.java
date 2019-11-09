package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DeliveryMap {

    private List<Delivery> deliveryRequests;

    private List<Delivery> impossibleDeliveries;

    private long warehouseNodeId;

    private int startDateInSeconds;


    public DeliveryMap() {
        this.deliveryRequests = new ArrayList<>();
        this.impossibleDeliveries = new ArrayList<>();
    }

    public void createDelivery(long pickUpId, long deliveryId, int pickUpDuration, int deliveryDuration) {
        Delivery newDelivery = new Delivery(pickUpId, deliveryId, pickUpDuration, deliveryDuration);
        this.deliveryRequests.add(newDelivery);
    }

    public void createImpossibleDelivery(long pickUpId, long deliveryId, int pickUpDuration, int deliveryDuration) {
        Delivery newDelivery = new Delivery(pickUpId, deliveryId, pickUpDuration, deliveryDuration);
        this.impossibleDeliveries.add(newDelivery);
    }

    public void createWarehouse(long positionId, int startDateInSeconds) {
        this.warehouseNodeId = positionId;
        this.startDateInSeconds = startDateInSeconds;
        if (this.startDateInSeconds < 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Delivery> getDeliveryList() {
        return deliveryRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeliveryMap that = (DeliveryMap) o;
        return warehouseNodeId == that.warehouseNodeId &&
            startDateInSeconds == that.startDateInSeconds &&
            Objects.equals(deliveryRequests, that.deliveryRequests) &&
            Objects.equals(impossibleDeliveries, that.impossibleDeliveries);
    }

    public List<Delivery> getImpossibleDeliveries() {
        return impossibleDeliveries;
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

    public int size(){
        return deliveryRequests.size();
    }

    public Delivery getDelivery(int i) {
        return deliveryRequests.get(i);
    }

    @Override
    public String toString() {
        return "DeliveryMap{" +
            "deliveryRequests=" + deliveryRequests +
            ", impossibleDeliveries=" + impossibleDeliveries +
            ", warehouseNodeId=" + warehouseNodeId +
            ", startDateInSeconds=" + startDateInSeconds +
            '}';
    }
}
