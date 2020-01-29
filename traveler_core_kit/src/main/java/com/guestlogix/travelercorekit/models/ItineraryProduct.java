package com.guestlogix.travelercorekit.models;

import java.util.Date;

class ItineraryProduct {
    private String id;
    private String orderId;
    private Date startDate;
    private Date endDate;
    private int duration;
    private Location startLocation;
    private Location endLocation;
    private int quantity;
    private boolean isAllDay;
}
