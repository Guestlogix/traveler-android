package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.Date;

public class DateRangeFilter implements Serializable {

    private Range<Date> dateRange;

    public DateRangeFilter(Range<Date> dateRange) {
        this.dateRange = dateRange;
    }

    public Range<Date> getDateRange() {
        return dateRange;
    }
}
