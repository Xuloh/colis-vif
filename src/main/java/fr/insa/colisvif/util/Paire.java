package fr.insa.colisvif.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

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

    public static void main(String[] args){
        Set<Long> s1 = new TreeSet<>();

        Set<Long> s2 = new TreeSet<>();

        Paire<Long, Set<Long>> p1 = new Paire<>(1L, s1);
        Paire<Long, Set<Long>> p2 = new Paire<>(1L, s2);

        System.out.println(p1.equals(p2));
        System.out.println(p2.equals(p1));
        System.out.println(p1.hashCode() == p2.hashCode());
    }

}