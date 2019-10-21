package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class PriceRange implements Serializable {
    private Range<Double> range;
    private Currency currency;

    public PriceRange(Range<Double> range, Currency currency) {
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
