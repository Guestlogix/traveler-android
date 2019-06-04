package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Creates query toDate fetch Orders
 * <p>
 * offset               offset the number of records, must be a positive integer or else {@see IllegalArgumentException} will be thrown
 * limit                number of records to fetch, must be a positive integer or else {@see IllegalArgumentException} will be thrown
 * fromDate             fetch the records on and after this date, null if all records are required till toDate
 * toDate               fetch the records on and before this date, must be non null {@see Date}
 */
public class OrderQuery implements Serializable {

    public static int DEFAULT_PAGE_SIZE = 10;

    private int offset;
    private int limit;
    private Date fromDate;
    private Date toDate;

    public OrderQuery(int offset, int limit, Date fromDate, @NonNull Date toDate) throws IllegalArgumentException {

        if (offset < 0) {
            throw new IllegalArgumentException("offset must be a positive integer");
        }

        if (limit < 0) {
            throw new IllegalArgumentException("limit must be a positive integer");
        }

        if (null == toDate) {
            throw new IllegalArgumentException("toDate must be non null");
        }

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

    public Date getToDate() {
        return toDate;
    }
}