package fr.insa.colisvif.model;

/**
 * Represent a Delivery by its pick up and drop off {@link Vertex} and its id.
 */
public class Delivery {
    private Vertex pickUp;

    private Vertex dropOff;

    private int id;

    /**
     * Creates a {@link Delivery}.
     *
     * @param id the id of the delivery.
     * @param pickUpNodeId the pick up {@link Node} id of the delivery.
     * @param dropOffNodeId the drop off {@link Node} id of the delivery.
     * @param pickUpDuration the pick up duration of the delivery.
     * @param dropOffDuration the drop off duration of the delivery
     */
    public Delivery(int id, long pickUpNodeId, long dropOffNodeId, int pickUpDuration, int dropOffDuration) throws IllegalArgumentException{
        pickUp = new Vertex(pickUpNodeId, Vertex.PICK_UP, pickUpDuration);
        dropOff = new Vertex(dropOffNodeId, Vertex.DROP_OFF, dropOffDuration);
        this.id = id;

        //TODO @Felix : Est-ce qu'un pickup et dropoff d'un même delivery peuvent être sur un même noeud?
        if (this.pickUp.getNodeId() == this.dropOff.getNodeId()) {
            throw new IllegalArgumentException("The drop off and the pick up must correspond to two different Nodes, got "
                    + pickUpNodeId + " (pick up node ID) and "
                    + dropOffNodeId + " (drop off node ID) and ");
        }
    }

    /**
     * Returns the id of the {@link Delivery}.
     *
     * @return the id of the {@link Delivery}.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the id of the {@link Delivery}.
     *
     * @return the id of the {@link Delivery}.
     */
    public long getPickUpNodeId() {
        return pickUp.getNodeId();
    }

    /**
     * Returns the drop off {@link Node} id of the {@link Delivery}.
     *
     * @return the drop off {@link Node} id of the {@link Delivery}.
     */
    public long getDropOffNodeId() {
        return dropOff.getNodeId();
    }

    /**
     * Returns the pick up {@link Node} id of the {@link Delivery}.
     *
     * @return the pick up {@link Node} id of the {@link Delivery}.
     */
    public int getPickUpDuration() {
        return pickUp.getDuration();
    }

    /**
     * Sets the pick up duration in seconds of the {@link Delivery}.
     *
     * @param pickUpDuration the time in seconds to perform the pick up.
     * @throws IllegalArgumentException if the pickUpDuration is not positive.
     */
    public void setPickUpDuration(int pickUpDuration) throws IllegalArgumentException {
        this.pickUp.setDuration(pickUpDuration);
    }

    /**
     * Returns the drop off {@link Node} id of the {@link Delivery}.
     *
     * @return the drop off {@link Node} id of the {@link Delivery}.
     */
    public int getDropOffDuration() {
        return dropOff.getDuration();
    }

    /**
     * Sets the drop off duration in seconds of the {@link Delivery}.
     *
     * @param dropOffDuration the time in seconds to perform the drop off.
     * @throws IllegalArgumentException if the deliveryDuration is not positive.
     */
    public void setDropOffDuration(int dropOffDuration) throws IllegalArgumentException {
        this.dropOff.setDuration(dropOffDuration);
    }

    /**
     * Returns a {@link String} representation of this {@link Delivery}.
     * @return a {@link String} representation of this {@link Delivery}.
     */
    @Override
    public String toString() {
        return "pickUpNodeId : " + pickUp.getNodeId() + " | deliveryNodeId : " + dropOff.getNodeId() + " | pickUpDuration : "
                + pickUp.getDuration() + " | deliveryDuration : " + dropOff.getDuration() + "\n";
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link Delivery}.
     * Only other {@link Delivery} are considered for comparison.
     * The method compares the ids, the pick up and drop off {@link Node} id
     * as well as the the pick up and drop off duration
     *
     * @param o the {@link Object} to compare this {@link Delivery} to
     *
     * @return <code>true</code> if o is a {@link Delivery} whose values are
     * "equal" to those of this {@link Delivery}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Delivery delivery = (Delivery) o;
        return  this.id == delivery.id
                && this.pickUp.getNodeId() == delivery.pickUp.getNodeId()
                && this.dropOff.getNodeId() == delivery.dropOff.getNodeId()
                && this.pickUp.getDuration() == delivery.pickUp.getDuration()
                && this.dropOff.getDuration() == delivery.dropOff.getDuration();
    }


}
