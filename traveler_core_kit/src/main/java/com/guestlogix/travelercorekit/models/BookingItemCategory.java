package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public enum BookingItemCategory implements Serializable {
    ACTIVITY("Activity"),
    TOUR("Tour"),
    SHOW("Show"),
    EVENT("Event"),
    NIGHTLIFE("Nightlife"),
    UNKNOWN("Unknown");

    private final String category;

    BookingItemCategory(String category) {
        this.category = category;
    }

    public static BookingItemCategory fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Activity":
                return ACTIVITY;
            case "Tour":
                return TOUR;
            case "Show":
                return SHOW;
            case "Event":
                return EVENT;
            case "Nightlife":
                return NIGHTLIFE;
            default:
                return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return this.category;
    }
}

