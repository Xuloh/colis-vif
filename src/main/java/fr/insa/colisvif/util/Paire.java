package fr.insa.colisvif.util;

import java.util.Objects;

public class Paire<T, U> {

    private final T first;
    private final U second;

    public Paire(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Paire<?, ?> paire = (Paire<?, ?>) o;
        return Objects.equals(first, paire.first) &&
                Objects.equals(second, paire.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }


}