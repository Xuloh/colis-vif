package fr.insa.colisvif.util;

// idea from https://stackoverflow.com/questions/6010843/java-how-to-store-data-triple-in-a-list

import java.util.Objects;

/**
 * A convenience class to hold together 3 values
 * @param <T> the type of the 1st value
 * @param <U> the type of the 2nd value
 * @param <V> the type of the 3rd value
 */
public class Triplet<T, U, V> {

    private final T first;

    private final U second;

    private final V third;

    /**
     * Creates a new {@link Triplet} with the given values
     *
     * @param first the 1st value
     * @param second the 2nd value
     * @param third the 3rd value
     */
    public Triplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link Triplet}.
     * Only other {@link Triplet} are considered for comparison.
     * The method compares the 3 values of this {@link Triplet}
     * with those of the other {@link Triplet} by calling their respective
     * <code>equals</code> method.
     *
     * @param o the {@link Object} to compare this {@link Triplet} to
     *
     * @return <code>true</code> if o is a {@link Triplet} whose values are
     * "equal" to those of this {@link Triplet}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(first, triplet.first)
               && Objects.equals(second, triplet.second)
               && Objects.equals(third, triplet.third);
    }

    /**
     * Returns the 1st value of this {@link Triplet}
     * @return the 1st value of this {@link Triplet}
     */
    public T getFirst() {
        return first;
    }

    /**
     * Returns the 2nd value of this {@link Triplet}
     * @return the 2nd value of this {@link Triplet}
     */
    public U getSecond() {
        return second;
    }

    /**
     * Returns the 3rd value of this {@link Triplet}
     * @return the 3rd value of this {@link Triplet}
     */
    public V getThird() {
        return third;
    }
}
