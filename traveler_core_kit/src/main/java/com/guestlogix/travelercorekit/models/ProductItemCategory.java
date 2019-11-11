package com.guestlogix.travelercorekit.models;

public enum ProductItemCategory {
    ACTIVITY("Activity"),
    TOUR("Tour"),
    SHOW("Show"),
    EVENT("Event"),
    NIGHTLIFE("Nightlife"),
    PARKING("Parking"),
    TRANSFERS("Transfers");

    private final String category;

    ProductItemCategory(String category) {
        this.category = category;
    }

    public static ProductItemCategory fromString(String value) throws IllegalArgumentException {
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
            case "Parking":
                return PARKING;
            case "transfers":
                return TRANSFERS;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return this.category;
    }
}

