package fr.insa.colisvif.util;

import java.util.Objects;

public class Quadruplet<T, U, V, W> {

    private final T first;
    private final U second;
    private final V third;
    private final W fourth;

    public Quadruplet(T first, U second, V third, W fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadruplet<?, ?, ?, ?> that = (Quadruplet<?, ?, ?, ?>) o;
        return Objects.equals(first, that.first) &&
                Objects.equals(second, that.second) &&
                Objects.equals(third, that.third) &&
                Objects.equals(fourth, that.fourth);
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
    public W getFourth() { return fourth; }
}
