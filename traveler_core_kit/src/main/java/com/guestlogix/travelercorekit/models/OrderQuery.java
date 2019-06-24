package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;

public class OrderQuery implements Serializable {
    private int offset;
    private int limit;
    private Date fromDate;
    private Date toDate;

    public OrderQuery(int offset, int limit, Date fromDate, @NonNull Date toDate) {
        this.offset = offset;
        this.limit = limit;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public @NonNull Date getToDate() {
        return toDate;
    }
}