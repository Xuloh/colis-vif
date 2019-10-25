package fr.insa.colisvif.model;

public class Delivery {

    private long pickUpNodeId;
    private long deliveryNodeId;
    private int pickUpDuration;
    private int deliveryDuration;
    private boolean isPicked;

    public Delivery(long pickUpNodeId, long deliveryNodeId, int pickUpDuration, int deliveryDuration) {
        this.pickUpNodeId = pickUpNodeId;
        this.deliveryNodeId = deliveryNodeId;
        this.pickUpDuration = pickUpDuration;
        this.deliveryDuration = deliveryDuration;
        this.isPicked = false;
    }

    public long getPickUpNodeId() {
        return pickUpNodeId;
    }

    public void setPickUpNodeId(long pickUpNodeId) {
        this.pickUpNodeId = pickUpNodeId;
    }

    public long getDeliveryNodeId() {
        return deliveryNodeId;
    }

    public void setDeliveryNodeId(long deliveryNodeId) {
        this.deliveryNodeId = deliveryNodeId;
    }

    public int getPickUpDuration() {
        return pickUpDuration;
    }

    public void setPickUpDuration(int pickUpDuration) {
        this.pickUpDuration = pickUpDuration;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public void setToPickedUp() {
        this.isPicked = true;
    }
}
