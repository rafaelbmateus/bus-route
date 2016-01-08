package com.rafaelmateus.bus.models;

import java.util.ArrayList;
import java.util.List;

public class Hour {
    private final String hour;

    private final List<String> listDeparture = new ArrayList<>();

    public Hour(String hour) {
        this.hour = hour;
    }

    public String getHour() {
        return this.hour;
    }

    public List<String> getListDeparture() {
        return this.listDeparture;
    }
}
