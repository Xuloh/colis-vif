package fr.insa.colisvif.model;

import java.util.Objects;

/**
 * Represents the {@link Section}, that has a length, a road name, a destination
 * and an origin.
 */
public class Section {
    private double length;

    private String roadName;

    private long destination;

    private long origin;

    /**
     * Constructor of Section.
     * @param length the length of the Section.
     * @param roadName the road name of the Section.
     * @param origin the {@link Node} id of the origin.
     * @param destination the {@link Node} id of the destination.
     */
    public Section(double length, String roadName, long origin,
                   long destination) {
        // TODO change destination origin to origin destination
        this.length = length;
        this.roadName = roadName;
        this.destination = destination;
        this.origin = origin;

        if (length <= 0) {
            throw new IllegalArgumentException();
        }
        //if(destination == origin) throw new IllegalArgumentException();
    }

    /**
     * Returns the length of the {@link Section}.
     *
     * @return the length of the {@link Section}.
     */
    public double getLength() {
        return this.length;
    }

    /**
     * Returns the road name of the {@link Section}.
     *
     * @return the road name of the {@link Section}.
     */
    public String getRoadName() {
        return this.roadName;
    }

    /**
     * Returns the origin ({@link Node} id) of the {@link Section}.
     *
     * @return the origin ({@link Node} id) of the {@link Section}.
     */
    public long getOrigin() {
        return this.origin;
    }

    /**
     * Returns the destination ({@link Node} id) of the {@link Section}.
     *
     * @return the destination ({@link Node} id) of the {@link Section}.
     */
    public long getDestination() {
        return this.destination;
    }

    /**
     * Returns a {@link String} representation of this {@link Section}.
     *
     * @return a {@link String} representation of this {@link Section}.
     */
    @Override
    public String toString() {
        return "Section{"
                + "length=" + length
                + ", roadName='" + roadName + '\''
                + ", origin=" + origin
                + ", destination=" + destination
                + '}';
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link Section}.
     * Only other {@link Section} are considered for comparison.
     * The method compares the lengths, the destination and origin {@link Node}
     * id as well as the road names.
     *
     * @param o the {@link Object} to compare this {@link Section} to
     *
     * @return <code>true</code> if o is a {@link Section} whose values are
     * "equal" to those of this {@link Section}
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Double.compare(section.length, length) == 0
                && destination == section.destination
                && origin == section.origin
                && Objects.equals(roadName, section.roadName);
    }

}
