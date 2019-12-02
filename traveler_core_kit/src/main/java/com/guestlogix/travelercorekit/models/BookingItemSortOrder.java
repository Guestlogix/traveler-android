package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

public enum BookingItemSortOrder {
    LOW_TO_HIGH("Asc", "Low to High"),
    HIGH_TO_LOW("Desc", "High to Low");

    private final String sortValue;
    private final String sortTitle;

    BookingItemSortOrder(String sortValue, String sortTitle) {
        this.sortValue = sortValue;
        this.sortTitle = sortTitle;
    }

    public static BookingItemSortOrder fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Asc":
                return LOW_TO_HIGH;
            case "Desc":
                return HIGH_TO_LOW;
            default:
                return null;
        }
    }

    public String getSortValue() {
        return sortValue;
    }

    @Override
    @NonNull
    public String toString() {
        return this.sortTitle;
    }
}

