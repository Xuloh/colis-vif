package fr.insa.colisvif.model;

import java.util.Objects;

/**
 * Represent either a drop off or a pick up point. It is attached to a {@link Node} id and a duration (the time to do the drop off or the pick up.
 */
public class Vertex implements Comparable<Vertex> {

    public static final boolean DROP_OFF = true;

    public static final boolean PICK_UP = false;

    private long nodeId;

    private boolean type; //false if it is a pick up and true if it is a drop off

    private int duration;

    /**
     * Constructor of Vertex.
     *
     * @param nodeId the {@link Node} id corresponding.
     * @param type the type, drop off or pick up.
     * @param duration the time to do the drop off or the pick up.
     * @throws IllegalArgumentException if the pickUpDuration/dropOffDuration is less than 0.
     */
    public Vertex(long nodeId, boolean type, int duration) throws IllegalArgumentException {
        if (duration < 0) {
            throw new IllegalArgumentException("The duration must be more or equal than 0 seconds, got " + duration);
        }
        this.nodeId = nodeId;
        this.type = type;
        this.duration = duration;
    }

    /**
     * Returns the duration of the {@link Vertex}.
     *
     * @return the duration of the {@link Vertex}.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the {@link Vertex}.
     *
     * @param durationInSeconds the duration in second, must be more or equal to 0.
     * @throws IllegalArgumentException if the duration is less than 0 seconds.
     */
    public void setDuration(int durationInSeconds) throws IllegalArgumentException {
        // Todo @Mathieu : needs test (new exception)
        if (durationInSeconds < 0) {
            throw new IllegalArgumentException("The duration in second needs to be more or equal than 0, got " + durationInSeconds);
        }

        this.duration = durationInSeconds;
    }

    /**
     * Returns the {@link Node} id corresponding to the {@link Vertex}.
     *
     * @return the {@link Node} id corresponding to the {@link Vertex}.
     */
    public long getNodeId() {
        return nodeId;
    }

    /**
     * Returns the type of the {@link Vertex}.
     *
     * @return the type of the {@link Vertex}.
     */
    public boolean getType() {
        return this.type;
    }

    /**
     * Returns <code>true</code> if the {@link Vertex} corresponds to a pick up.
     *
     * @return <code>true</code> if the {@link Vertex} corresponds to a pick up.
     */
    public boolean isPickUp() {
        return Objects.equals(type, Vertex.PICK_UP);
    }

    /**
     * Returns <code>true</code> if the {@link Vertex} corresponds to a drop off.
     *
     * @return <code>true</code> if the {@link Vertex} corresponds to a drop off.
     */
    public boolean isDropOff() {
        return Objects.equals(type, Vertex.DROP_OFF);
    }

    /**
     * Performs a comparison with the {@link Vertex} vertex.
     * @param vertex the {@link Vertex} to compare to.
     * @return 0 if they are the same, -1 if this < vertex, 1 if this > vertex.
     */
    @Override
    public int compareTo(Vertex vertex) {
        if (this.nodeId == vertex.nodeId) {
            if (this.type == vertex.type) {
                if (this.duration == vertex.duration) {
                    return 0;
                }
                return this.duration - vertex.duration;
            }
            if (this.type) {
                return 1;
            }
            return -1;
        }
        return (int) (this.nodeId - vertex.nodeId);
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link Vertex}.
     * Only other {@link Vertex} are considered for comparison.
     * The method compares the {@link Node} id, the type and the duration.
     *
     * @param o the {@link Object} to compare this {@link Vertex} to
     *
     * @return <code>true</code> if o is a {@link Vertex} whose values are
     * "equal" to those of this {@link Vertex}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return nodeId == vertex.nodeId
                && type == vertex.type
                && duration == vertex.duration;
    }

    /**
     * Returns the hashCode of the current {@link Vertex}. It is based on the id and the type, not the duration.
     *
     * @return the hashCode of the current {@link Vertex}. It is based on the id and the type, not the duration.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nodeId, type);
    }

    /**
     * Returns a {@link String} representation of this {@link Vertex}.
     *
     * @return a {@link String} representation of this {@link Vertex}.
     */
    @Override
    public String toString() {
        return "Vertex{"
                + "id=" + nodeId
                + ", type=" + type
                + ", durationInSeconds=" + duration
                + '}';
    }
}
