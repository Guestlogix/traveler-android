package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class Range<T extends Comparable<? super T>> implements Serializable {
    private final T lower;
    private final T upper;

    public Range(final T lower,final T upper) {
        this.lower = lower;
        this.upper = upper;

        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException("lower must be less than or equal to upper");
        }
    }

    public T getLower() {
        return lower;
    }

    public T getUpper() {
        return upper;
    }
}
