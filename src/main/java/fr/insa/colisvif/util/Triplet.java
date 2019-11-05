package fr.insa.colisvif.util;

// TODO :
//  -> credit https://stackoverflow.com/questions/6010843/java-how-to-store-data-triple-in-a-list

import java.util.Objects;

public class Triplet<T, U, V> {

    private final T first;
    private final U second;
    private final V third;

    public Triplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(first, triplet.first) &&
                Objects.equals(second, triplet.second) &&
                Objects.equals(third, triplet.third);
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
}