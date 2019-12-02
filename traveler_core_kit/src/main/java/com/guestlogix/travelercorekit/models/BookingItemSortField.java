package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

public enum BookingItemSortField {
    SORT_BY_PRICE("Price"),
    SORT_BY_TITLE("Title");


    private final String sortOption;

    BookingItemSortField(String sortOption) {
        this.sortOption = sortOption;
    }

    public static BookingItemSortField fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Price":
                return SORT_BY_PRICE;
            case "Title":
                return SORT_BY_TITLE;
            default:
                return null;
        }
    }

    @Override
    @NonNull
    public String toString() {
        return this.sortOption;
    }
}

