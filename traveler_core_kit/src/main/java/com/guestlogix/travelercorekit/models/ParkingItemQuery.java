package com.guestlogix.travelercorekit.models;

import java.util.Date;

public class ParkingItemQuery implements SearchQuery {
    private String airportIATA;
    private Range<Date> dateRange;
    private BoundingBox boundingBox;
    private int offset;
    private int limit;

    public ParkingItemQuery(
            String airportIATA,
            Range<Date> dateRange,
            BoundingBox boundingBox,
            int offset,
            int limit) {
        this.airportIATA = airportIATA;
        this.dateRange = dateRange;
        this.boundingBox = boundingBox;
        this.offset = offset;
        this.limit = limit;
    }

    public ParkingItemQuery(ParkingItemSearchParameters parameters) {
        this.airportIATA = parameters.getAirportIATA();
        this.dateRange = parameters.getDateRange();
        this.boundingBox = parameters.getBoundingBox();
        this.offset = 0;
        this.limit = 10;
    }

    public String getAirportIATA() {
        return airportIATA;
    }

    public Range<Date> getDateRange() {
        return dateRange;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getLimit() {
        return limit;
    }
}
