package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BookingItemSort implements Serializable {

    private BookingItemSortOrder sortOrder;
    private BookingItemSortField sortField;

    public BookingItemSort(BookingItemSortField sortField, BookingItemSortOrder sortOrder) {
        this.sortOrder = sortOrder;
        this.sortField = sortField;
    }

    public BookingItemSortOrder getSortOrder() {
        return sortOrder;
    }

    public BookingItemSortField getSortField() {
        return sortField;
    }

    public static List<BookingItemSort> getAllPossibleSortTypes() {
        ArrayList<BookingItemSort> sortTypes = new ArrayList<>();

        for (BookingItemSortField bookingItemSortField : EnumSet.allOf(BookingItemSortField.class)) {
            for (BookingItemSortOrder bookingItemSortOrder : EnumSet.allOf(BookingItemSortOrder.class)) {
                sortTypes.add(new BookingItemSort(bookingItemSortField, bookingItemSortOrder));
            }
        }
        return sortTypes;
    }

    @Override
    @NonNull
    public String toString() {
        return this.sortField + ": " + this.sortOrder;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof BookingItemSort) {
            return ((BookingItemSort) obj).sortField == this.sortField && ((BookingItemSort) obj).sortOrder == this.sortOrder;
        }
        return false;
    }
}

