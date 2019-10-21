package com.guestlogix.travelercorekit.models;

public enum CatalogItemCategory {
    ACTIVITY("Activity"),
    TOUR("Tour"),
    SHOW("Show"),
    EVENT("Event"),
    NIGHTLIFE("Nightlife");

    private final String category;

    CatalogItemCategory(String category) {
        this.category = category;
    }

    public static CatalogItemCategory fromString(String value) throws IllegalArgumentException {
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
                throw new IllegalArgumentException("Unknown CatalogItemCategory");
        }
    }

    @Override
    public String toString() {
        return this.category;
    }
}

