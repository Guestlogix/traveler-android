package com.guestlogix.travelercorekit.models;

public enum Category {
    ACTIVITY,
    TOUR,
    SHOW,
    EVENT,
    TRANSFER,
    PARKING,
    NIGHTLIFE,
    DINING;

    public static Category fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Activity":
                return ACTIVITY;
            case "Tour":
                return TOUR;
            case "Show":
                return SHOW;
            case "Event":
                return EVENT;
            case "Transfer":
                return TRANSFER;
            case "Parking":
                return PARKING;
            case "Nightlife":
                return NIGHTLIFE;
            case "Dining":
                return DINING;
            default:
                throw new IllegalArgumentException("Unknown Category");
        }
    }
}

