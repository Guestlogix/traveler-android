package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Creates query toDate fetch Orders
 * <p>
 * offset               offset the number of records
 * limit                number of records to fetch
 * fromDate             fetch the records on and after this date
 * toDate               fetch the records on and before this date
 */
public class OrderQuery implements Serializable {

    public static int DEFAULT_PAGE_SIZE = 10;

    private int offset;
    private int limit;
    private Date fromDate;
    private Date toDate;

    public OrderQuery(int offset, int limit, Date fromDate, Date toDate) {
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
