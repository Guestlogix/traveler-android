package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.Date;

/**
 * User this class to fetch Orders
 * <p>
 * offset                offset the number of records
 * limit                number of records to fetch
 * from                fetch the records on and after this date
 * to                  fetch the records on and before this date
 */
public class OrderQuery implements Serializable {

    public static Integer DEFAULT_PAGE_SIZE = 10;

    private Integer offset;
    private Integer limit;
    private Date from;
    private Date to;

    public OrderQuery(Integer offset, Integer limit, Date from, Date to) {
        this.offset = offset;
        this.limit = limit;
        this.from = from;
        this.to = to;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
