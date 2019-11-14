package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdException;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a Node, formed with an id, a longitude and latitude in degrees, and a {@link List} of successors ({@link Section}).
 */
public class Node {

    private long id;

    private double longitude;

    private double latitude;

    private List<Section> successors;

    /**
     * Constructor of Node.
     *
     * @param id the id of the Node.
     * @param latitude the longitude of the Node.
     * @param longitude the longitude of the Node.
     * @throws IllegalArgumentException if the latitude is not between -90 and 90 and/or the longitude is not between -180 and 180.
     */
    public Node(long id, double latitude, double longitude) throws IllegalArgumentException {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.successors = new ArrayList<>();

        if (this.latitude < -90 || this.latitude > 90) {
            throw new IllegalArgumentException("The latitude must be between -90 and 90, got " + latitude);
        }
        if (this.longitude > 180 || this.longitude < -180) {
            throw new IllegalArgumentException("The longitude must be between -180 and 180, got " + longitude);
        }
    }

    /**
     * Returns the longitude.
     *
     * @return the longitude.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Returns the latitude.
     *
     * @return the latitude.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Returns the id.
     *
     * @return the id.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Returns the {@link List} of successors ({@link Section}).
     *
     * @return the {@link List} of successors ({@link Section}).
     */
    public List<Section> getSuccessors() {
        return this.successors;
    }

    /**
     * Add a {@link Section} to the {@link List} of successors of the current {@link Node}.
     * @param section the {@link Section} to add.
     * @throws IdException if the origin of the new {@link Section} is different from the id of the current {@link Node}.
     */
    public void addToSuccessors(Section section) throws IdException {
        if (section.getOrigin() == this.id) {
            this.successors.add(section);
        } else {
            throw new IdException("The origin of the new section does not correspond to the origin of this Node. Got " + section.getOrigin() + " instead of " + this.id);
        }
    }

    /**
     * Sets the longitude of the current {@link Node}.
     * @param longitude the longitude to set.
     * @throws IllegalArgumentException if the longitude is less or more than 180
     */
    public void setLongitude(double longitude) {
        if (longitude > 180 || longitude < -180) {
            throw new IllegalArgumentException("The longitude must be between -180 and 180, got " + longitude);
        }
        this.longitude = longitude;
    }


    /**
     * Sets the latitude of the current {@link Node}.
     * @param latitude the latitude to set.
     * @throws IllegalArgumentException if the latitude is less or more than 90
     */
    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("The latitude must be between -90 and 90, got " + latitude);
        }
        this.latitude = latitude;
    }

    /**
     * Returns a {@link String} representation of this {@link Node}.
     * @return a {@link String} representation of this {@link Node}.
     */
    @Override
    public String toString() {
        String result = "ID : " + id + " | Latitude : " + latitude + " | Longitude : " + longitude + "\n";
        return result;
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link Node}.
     * Only other {@link Node} are considered for comparison.
     * The method compares the id of the two {@link Node};
     *
     * @param o the {@link Object} to compare this {@link Node} to
     *
     * @return <code>true</code> if o is a {@link Node} whose values are
     * "equal" to those of this {@link Node}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node node = (Node) o;
        return id == node.id;
    }
}
