package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public enum BookingItemCategory implements Serializable {
    ACTIVITY("Activity"),
    TOUR("Tour"),
    SHOW("Show"),
    EVENT("Event"),
    NIGHTLIFE("Nightlife");

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
                return null; //TODO:we should throw exception for unknown types. we did it becuase of category problem in anyItemMappingFactory
        }
    }

    @Override
    public String toString() {
        return this.category;
    }
}

