package com.guestlogix.travelercorekit.models;

public enum ProductItemCategory {
    ACTIVITY("Activity"),
    TOUR("Tour"),
    SHOW("Show"),
    EVENT("Event"),
    NIGHTLIFE("Nightlife");

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
            default:
                return null;
//                throw new IllegalArgumentException("Unknown ProductItemCategory");
        }
    }

    @Override
    public String toString() {
        return this.category;
    }
}

