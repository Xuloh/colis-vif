package fr.insa.colisvif.model;

import java.util.Objects;

public class Delivery {
    private PickUp pickUp;
    private DropOff dropOff;

    public Delivery(long pickUpNodeId, long dropOffNodeId, int pickUpDuration, int dropOffDuration) {
        this.pickUp.setNodeId(pickUpNodeId);
        this.dropOff.setNodeId(dropOffNodeId);
        this.pickUp.setDuration(pickUpDuration);
        this.dropOff.setDuration(dropOffDuration);

        if (this.pickUp.getNodeId() == this.dropOff.getNodeId()) {
            throw new IllegalArgumentException();
        }

        if (this.pickUp.getDuration() < 0 || this.dropOff.getDuration() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public long getPickUpNodeId() {
        return pickUp.getNodeId();
    }

    public void setPickUpNodeId(long pickUpNodeId) {
        this.pickUp.setNodeId(pickUpNodeId);
    }

    public long getDropOffNodeId() {
        return dropOff.getNodeId();
    }

    public void setDropOffNodeId(long deliveryNodeId) {
        this.dropOff.setNodeId(deliveryNodeId);
    }

    public int getPickUpDuration() {
        return pickUp.getDuration();
    }

    public void setPickUpDuration(int pickUpDuration) {
        this.pickUp.setDuration(pickUpDuration);
    }

    public int getDropOffDuration() {
        return dropOff.getDuration();
    }

    public void setDropOffDuration(int deliveryDuration) {
        this.dropOff.setDuration(deliveryDuration);
    }

    @Override
    public String toString() {
        String result = "pickUpNodeId : " + pickUp.getNodeId() + " | deliveryNodeId : " + dropOff.getNodeId() + " | pickUpDuration : "
                + pickUp.getDuration() + " | deliveryDuration : " + dropOff.getDuration() + "\n";
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Delivery delivery = (Delivery) o;
        return this.pickUp.getNodeId() == delivery.pickUp.getNodeId() &&
            this.dropOff.getNodeId() == delivery.dropOff.getNodeId() &&
            this.pickUp.getDuration() == delivery.pickUp.getDuration() &&
            this.dropOff.getDuration() == delivery.dropOff.getDuration();
    }


}
