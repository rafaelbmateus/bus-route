package com.rafaelmateus.bus.models;

public class Route {

    private final int id;

    private final String shortName;

    private final String longName;

    public Route(int id, String shortName, String longName) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
    }

    public int getId() {
        return this.id;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getLongName() {
        return this.longName;
    }
}
