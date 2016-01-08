package com.rafaelmateus.bus.models;

public class RouteStop {

    private final int id;

    private final String name;

    private final int sequence;

    public RouteStop(int id, String name, int sequence) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getSequence() {
        return this.sequence;
    }
}
