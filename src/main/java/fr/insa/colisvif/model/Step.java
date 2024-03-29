package fr.insa.colisvif.model;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Represents a {@link Step}: all the road before a {@link Vertex} (drop off or
 * pick up) and the {@link Vertex}.It is composed of a {@link LinkedList} of
 * {@link Section}, a delivery ID, a type (pick up or drop off), an arrival
 * date (in seconds, from the start of the round, and a duration of the pick
 * up or the drop off.
 */
public class Step {
    private LinkedList<Section> sections;

    private int deliveryID;

    //the date when the delivery man will get to delivery point
    private int arrivalDate;

    private int initialArrivalDate;

    private Vertex arrival;


    //TODO - @Sophie et @Guilhem virer ces deux trucs
    @Deprecated
    //false if it is a pick up and true if it is a drop off
    private boolean type;

    @Deprecated
    //the duration of the pick up or the drop off, NOT THE TRAVEL TIME
    private int duration;

    /**
     * The constructor of Step.
     *
     * @param vertex     the {@link Vertex} (pick up or drop off).
     * @param deliveryID the deliveryID corresponding to the {@link Delivery}
     *                   of the {@link Vertex}.
     */
    public Step(Vertex vertex, int deliveryID, int arrivalDate) {
        sections = new LinkedList<>();
        arrival = vertex;
        this.type = vertex.getType();
        this.duration = vertex.getDuration();
        this.deliveryID = deliveryID;
        this.arrivalDate = arrivalDate;
        this.initialArrivalDate = arrivalDate;
    }

    /**
     * Returns the type of the Step.
     *
     * @return the type of the Step.
     */
    public boolean getType() {
        return this.type;
    }

    /**
     * Returns the arrival date (in seconds) of the {@link Step}.
     *
     * @return the arrival date (in seconds) of the {@link Step}.
     */
    public int getArrivalDate() {
        return arrivalDate;
    }

    /**
     * Sets the arrival date (in seconds) of the {@link Step}.
     *
     * @param arrivalDate the arrival date (in seconds) of the {@link Step}.
     * @throws IllegalArgumentException if the arrival date is under 0 second.
     */
    public void setArrivalDate(int arrivalDate)
            throws IllegalArgumentException {
        if (arrivalDate < 0) {
            throw new IllegalArgumentException("The arrival date must be"
                    + " equal or over 0 second, got " + arrivalDate);
        }
        this.arrivalDate = arrivalDate;
    }

    public int getInitialArrivalDate() {
        return initialArrivalDate;
    }

    public void setInitialArrivalDate() {
        this.initialArrivalDate = this.arrivalDate;
    }

    public long getArrivalNodeId() {
        return arrival.getNodeId();
    }

    public void setArrivalNodeId(long nodeId) {
        arrival.setNodeId(nodeId);
    }

    /**
     * Returns the duration (in seconds) of the pick up or drop off.
     *
     * @return the duration (in seconds) of the pick up or drop off.
     */
    public int getDuration() {
        return arrival.getDuration();
    }

    /**
     * Returns the delivery ID of the {@link Step}.
     *
     * @return the delivery ID of the {@link Step}.
     */
    public int getDeliveryID() {
        return deliveryID;
    }

    /**
     * Sets the duration in seconds of the {@link Step}.
     *
     * @param duration the duration in seconds of the {@link Step}.
     * @throws IllegalArgumentException if the duration in seconds is under
     * 0 seconds.
     */
    public void setDuration(int duration) throws IllegalArgumentException {
        if (duration < 0) {
            throw new IllegalArgumentException("The duration in seconds must "
                    + "be equal or over 0 second, got " + duration);
        }
        this.duration = duration;
        arrival.setDuration(duration);
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    /**
     * Returns the {@link LinkedList} of {@link Section} of the {@link Step}.
     *
     * @return the {@link LinkedList} of {@link Section} of the {@link Step}.
     */
    public LinkedList<Section> getSections() {
        return sections;
    }

    public void setSections(LinkedList<Section> sections) {
        this.sections = new LinkedList<>(sections);
    }

    /**
     * Returns true if the {@link Step} corresponds to a pick up.
     *
     * @return <code>true</code> if the {@link Step} corresponds to a pick up.
     */
    public boolean isPickUp() {
        return arrival.isPickUp();
    }

    /**
     * Returns true if the {@link Step} corresponds to a drop off.
     *
     * @return <code>true</code> if the {@link Step} corresponds to a drop off.
     */
    public boolean isDropOff() {
        return arrival.isDropOff();
    }

    /**
     *
     *
     * @return
     */
    public Vertex getArrival() {
        return arrival;
    }

    /**
     * Add a section to the front of the {@link LinkedList} of {@link Section}
     * of the {@link Step}.
     *
     * @param section the {@link Section} to add.
     * @throws IllegalArgumentException if the new {@link Section}'s origin
     * {@link Node} id does not correspond to the lastly added {@link Section}'s
     * destination {@link Node} id.
     */
    public void addSection(Section section) throws IllegalArgumentException {
        if (sections.size() > 0
                && sections.getFirst().getOrigin()
                    != section.getDestination()) {
            throw new IllegalArgumentException("The origin of the new "
                    + "Section does not correspond to the last Section in "
                    + "the list. Got "
                    + section.getOrigin() + " instead of "
                    + sections.getFirst().getDestination());
        }
        sections.addFirst(section);
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link Step}.
     * Only other {@link Step} are considered for comparison.
     * The method compares the deliveryID, the type, the arrivalDate, the
     * duration and the {@link LinkedList} of {@link Section}.
     *
     * @param o the {@link Object} to compare this {@link Step} to
     * @return <code>true</code> if o is a {@link Step} whose values are
     * "equal" to those of this {@link Step}
     */


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Step step = (Step) o;
        return deliveryID == step.deliveryID
                && Objects.equals(arrival, step.arrival)
                && arrivalDate == step.arrivalDate
                && Objects.equals(sections, step.sections);
    }

    /**
     * Returns a {@link String} representation of this {@link Step}.
     *
     * @return a {@link String} representation of this {@link Step}.
     */
    @Override
    public String toString() {
        return "Step{"
                + "sections=" + sections
                + ", deliveryID=" + deliveryID
                + ", arrivalDate=" + arrivalDate
                + ", initialArrivalDate=" + initialArrivalDate
                + ", arrival=" + arrival + '}';
    }
}
