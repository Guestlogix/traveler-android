package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PriceRangeFilter) {
            return ((PriceRangeFilter) obj).range.getLower().doubleValue() == this.range.getLower().doubleValue() &&
                    ((PriceRangeFilter) obj).range.getUpper().doubleValue() == this.range.getUpper().doubleValue() &&
                    ((PriceRangeFilter) obj).currency == this.currency;
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return range.getLower() + " - " + range.getUpper();
    }
}
