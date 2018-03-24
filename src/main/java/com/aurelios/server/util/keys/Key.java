package com.aurelios.server.util.keys;

public class Key<T> {

    private T value;

    public Key(T value){this.value = value;}

    public T getValue() {
        return value;
    }

}
