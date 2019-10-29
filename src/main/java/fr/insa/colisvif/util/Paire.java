package fr.insa.colisvif.util;

public class Paire<T, U> {

    private final T first;
    private final U second;

    public Paire(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
}