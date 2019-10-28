package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class PriceRangeFilter implements Serializable {
    private Range<Double> range;
    private Currency currency;

    public PriceRangeFilter(Range<Double> range, @NonNull Currency currency) {
        this.range = range;
        this.currency = currency;
    }

    public Range<Double> getRange() {
        return range;
    }

    public Currency getCurrency() {
        return currency;
    }
}
