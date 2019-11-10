package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Create and contains all the {@link Delivery} in a {@link List}, the warehouse {@link Node} id and the starting date in seconds.
 */
public class DeliveryMap {
    private List<Delivery> deliveryRequests;

    private long warehouseNodeId;

    private int startDateInSeconds;

    /**
     * Constructor of the {@link DeliveryMap}
     */
    public DeliveryMap() {
        this.deliveryRequests = new ArrayList<>();
    }

    /**
     * Create a {@link Delivery} out of a pick up {@link Node} id, a delivery {@link Node} id, a pick up duration and a delivery duration (in seconds).
     * It also attaches an id to the {@link Delivery}.
     *
     * @param id : the id of the {@link Delivery}.
     * @param pickUpNodeId the {@link Node} id of the pick up.
     * @param deliveryNodeId the {@link Node} id of the drop off.
     * @param pickUpDuration the duration of the pick up in seconds.
     * @param deliveryDuration the duration of the drop off in seconds.
     * @throws IllegalArgumentException if the pickUpDuration / deliveryDuration is less or equal to 0 seconds or the pickUpNodeId is equal to the deliveryNodeId.
     */
    public void createDelivery(int id, long pickUpNodeId, long deliveryNodeId, int pickUpDuration, int deliveryDuration) throws IllegalArgumentException {
        //TODO Le pickUpNodeId et le deliveryNodeId doivent correspondre à un noeud existant.
        Delivery newDelivery = new Delivery(id, pickUpNodeId, deliveryNodeId, pickUpDuration, deliveryDuration);
        this.deliveryRequests.add(newDelivery);
    }

    /**
     * Creates the warehouse.
     *
     * @param positionId the position of the warehouse
     * @param startDateInSeconds the starting date in seconds, when the rider can begin its round.
     * @throws IllegalArgumentException when the startDateInSeconds is less than 0 seconds.
     */
    public void createWarehouse(long positionId, int startDateInSeconds) throws IllegalArgumentException {
        //TODO La warehouse doit correspondre à un noeud.
        this.warehouseNodeId = positionId;
        this.startDateInSeconds = startDateInSeconds;
        if (startDateInSeconds < 0) {

            throw new IllegalArgumentException("The startDateInSeconds must be more or equal than 0, got " + startDateInSeconds);
        }
    }

    /**
     * Returns the {@link List} of all {@link Delivery}.
     *
     * @return the {@link List} of all {@link Delivery}.
     */
    public List<Delivery> getDeliveryList() {
        return deliveryRequests;
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link DeliveryMap}.
     * Only other {@link DeliveryMap} are considered for comparison.
     * The method compares the warehouse {@link Node} id, the start date in seconds
     * and the {@link List} of {@link Delivery}.
     *
     * @param o the {@link Object} to compare this {@link DeliveryMap} to
     *
     * @return <code>true</code> if o is a {@link DeliveryMap} whose values are
     * "equal" to those of this {@link DeliveryMap}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeliveryMap that = (DeliveryMap) o;
        return warehouseNodeId == that.warehouseNodeId
                && startDateInSeconds == that.startDateInSeconds
                && Objects.equals(deliveryRequests, that.deliveryRequests);
    }

    /**
     * Returns the warehouse {@link Node} id.
     *
     * @return the warehouse {@link Node} id.
     */
    public long getWarehouseNodeId() {
        return warehouseNodeId;
    }

    /**
     * Returns the start date in seconds.
     *
     * @return the start date in seconds.
     */
    public int getStartDateInSeconds() {
        return startDateInSeconds;
    }

    /**
     * Returns the size of the {@link List} of {@link Delivery}.
     *
     * @return the size of the {@link List} of {@link Delivery}.
     */
    public int size() {
        return deliveryRequests.size();
    }

    /**
     * Get the {@link Delivery} from the {@link List} of {@link Delivery} at index i.
     * @param i the index of the {@link Delivery} in the {@link List} of {@link Delivery}.
     * @return the {@link Delivery} in the {@link List} of {@link Delivery} at index i.
     * @throws IndexOutOfBoundsException if the index is not in the bounds of the {@link List}.
     */
    public Delivery getDelivery(int i) throws IndexOutOfBoundsException {
        return deliveryRequests.get(i);
    }

    /**
     * Returns a {@link String} representation of this {@link DeliveryMap}.
     * @return a {@link String} representation of this {@link DeliveryMap}.
     */
    @Override
    public String toString() {
        return "DeliveryMap{"
                + "deliveryRequests="
                + deliveryRequests
                + ", warehouseNodeId="
                + warehouseNodeId
                + ", startDateInSeconds="
                + startDateInSeconds
                + '}';
    }
}
