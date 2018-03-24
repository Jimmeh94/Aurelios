package com.aurelios.server.util.misc;

public class Pair<T, K> {

    private T first;
    private K second;

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public K getSecond() {
        return second;
    }

    public boolean has(Object a) {
        return first == a || first.equals(a) || second == a || second.equals(a);
    }

    public void setSecond(K second) {
        this.second = second;
    }
}
