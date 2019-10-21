package fr.insa.colisvif.util;

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

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
    public W getFourth() { return fourth; }
}
