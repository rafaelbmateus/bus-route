package com.rafaelmateus.bus.models;

import java.util.TreeMap;

public class Day {

    private final String day;

    private final TreeMap<String, Hour> hashMapHours = new TreeMap<>();

    public Day(String day) {
        this.day = day;
    }

    public String getDay() {
        return this.day;
    }

    public TreeMap<String, Hour> getHashMapHours() {
        return this.hashMapHours;
    }

    @Override
    public String toString() {
        return this.day;
    }
}
