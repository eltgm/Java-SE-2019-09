package ru.otus.calleridclient.models;

public class RingTypeSingleton {
    private static RingTypeSingleton instance;
    public int ringerMode = 0;

    private RingTypeSingleton() {
    }

    public static RingTypeSingleton getInstance() {
        if (instance == null) {
            instance = new RingTypeSingleton();
        }
        return instance;
    }
}
