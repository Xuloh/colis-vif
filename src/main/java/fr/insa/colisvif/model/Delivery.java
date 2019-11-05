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

        if (this.pickUpNodeId == this.deliveryNodeId) {
            throw new IllegalArgumentException();
        }

        if (this.pickUpDuration <= 0 || this.deliveryDuration <= 0) {
            throw new IllegalArgumentException();
        }
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

    public boolean isPicked() { return isPicked; }

    public void setToPickedUp(boolean value) {
        this.isPicked = value;
    }

    @Override
    public String toString() {
        String result = "pickUpNodeId : " + pickUpNodeId + " | deliveryNodeId : " + deliveryNodeId + " | pickUpDuration : "
                + pickUpDuration + " | deliveryDuration : " + deliveryDuration + " | isPicked : " + isPicked + "\n";
        return result;
    }

}
