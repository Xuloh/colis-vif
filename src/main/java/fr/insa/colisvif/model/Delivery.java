package fr.insa.colisvif.model;

public class Delivery {
    private Vertex pickUp;

    private Vertex dropOff;

    private int id;

    public Delivery(int id, long pickUpNodeId, long dropOffNodeId, int pickUpDuration, int dropOffDuration) {
        pickUp = new Vertex(pickUpNodeId, Vertex.PICK_UP, pickUpDuration);
        dropOff = new Vertex(dropOffNodeId, Vertex.DROP_OFF, dropOffDuration);
        this.id = id;

        if (this.pickUp.getNodeId() == this.dropOff.getNodeId()) {
            throw new IllegalArgumentException();
        }

        if (this.pickUp.getDuration() < 0 || this.dropOff.getDuration() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getId() {
        return this.id;
    }

    public Vertex getPickUp() {
        return this.pickUp;
    }

    public Vertex getDropOff() {
        return this.dropOff;
    }

    public long getPickUpNodeId() {
        return pickUp.getNodeId();
    }

    public long getDropOffNodeId() {
        return dropOff.getNodeId();
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
        return this.pickUp.getNodeId() == delivery.pickUp.getNodeId()
                && this.dropOff.getNodeId() == delivery.dropOff.getNodeId()
                && this.pickUp.getDuration() == delivery.pickUp.getDuration()
                && this.dropOff.getDuration() == delivery.dropOff.getDuration();
    }


}
