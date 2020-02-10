package com.guestlogix.traveleruikit.itinerary;

import com.guestlogix.travelercorekit.models.ItineraryItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ItineraryGroup implements Serializable {
    private Date groupDate;
    private List<ItineraryItem> itineraries;

    public ItineraryGroup(Date groupDate, List<ItineraryItem> itineraries) {
        this.groupDate = groupDate;
        this.itineraries = itineraries;
    }

    public Date getGroupDate() {
        return groupDate;
    }

    public List<ItineraryItem> getItineraries() {
        return itineraries;
    }
}
