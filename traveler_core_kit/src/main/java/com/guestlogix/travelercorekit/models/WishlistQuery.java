package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.Date;

public class WishlistQuery implements Serializable {
    private int offset;
    private int limit;
    private Date fromDate;
    private Date toDate;

    /**
     * Initializes a WishlistQuery
     * @param offset Pagination offset, default to 0
     * @param limit Pagination limit, defualt to 10
     * @param fromDate CatalogItems that were wishlisted after this date, default to null
     * @param toDate CatalogItems that were wishlisted before this date, passing null will default to NOW
     */
    public WishlistQuery(int offset, int limit, Date fromDate, Date toDate) {
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
