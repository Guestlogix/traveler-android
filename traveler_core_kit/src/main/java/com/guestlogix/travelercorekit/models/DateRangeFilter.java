package com.guestlogix.travelercorekit.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public class DateRangeFilter implements Serializable {
    @Nullable
    private Date startDate;
    @Nullable
    private Date endDate;

    public DateRangeFilter(@Nullable Date startDate, @Nullable Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Nullable
    public Date getStartDate() {
        return startDate;
    }

    @Nullable
    public Date getEndDate() {
        return endDate;
    }
}
