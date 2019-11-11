package fr.insa.colisvif.util;

import java.util.Objects;

/**
 * A convenience class to hold together 4 values
 * @param <T> the type of the 1st value
 * @param <U> the type of the 2nd value
 * @param <V> the type of the 3rd value
 * @param <W> the type of the 4th value
 */
public class Quadruplet<T, U, V, W> {

    private final T first;

    private final U second;

    private final V third;

    private final W fourth;

    /**
     * Creates a new {@link Quadruplet} with the given values
     *
     * @param first the 1st value
     * @param second the 2nd value
     * @param third the 3rd value
     * @param fourth the 4th value
     */
    public Quadruplet(T first, U second, V third, W fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    /**
     * Determines if the given {@link Object} is "equal"
     * to this {@link Quadruplet}.
     * Only other {@link Quadruplet} are considered for comparison.
     * The method compares the 4 values of this {@link Quadruplet}
     * with those of the other {@link Quadruplet} by calling their respective
     * <code>equals</code> method.
     *
     * @param o the {@link Object} to compare this {@link Quadruplet} to
     *
     * @return <code>true</code> if o is a {@link Quadruplet} whose values are
     * "equal" to those of this {@link Quadruplet}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Quadruplet<?, ?, ?, ?> that = (Quadruplet<?, ?, ?, ?>) o;
        return Objects.equals(first, that.first)
               && Objects.equals(second, that.second)
               && Objects.equals(third, that.third)
               && Objects.equals(fourth, that.fourth);
    }

    /**
     * Returns the 1st value of this {@link Quadruplet}
     * @return the 1st value of this {@link Quadruplet}
     */
    public T getFirst() {
        return first;
    }

    /**
     * Returns the 2nd value of this {@link Quadruplet}
     * @return the 2nd value of this {@link Quadruplet}
     */
    public U getSecond() {
        return second;
    }

    /**
     * Returns the 3rd value of this {@link Quadruplet}
     * @return the 3rd value of this {@link Quadruplet}
     */
    public V getThird() {
        return third;
    }

    /**
     * Returns the 4th value of this {@link Quadruplet}
     * @return the 4th value of this {@link Quadruplet}
     */
    public W getFourth() {
        return fourth;
    }

    /**
     * Returns a {@link String} representation of this {@link Quadruplet}.
     * @return a {@link String} representation of this {@link Quadruplet}.
     */
    @Override
    public String toString() {
        return ("(" + first + "," + second + "," + third + "," + fourth + ")");
    }
}
