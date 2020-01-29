package com.guestlogix.traveleruikit.itinerary;

import com.guestlogix.travelercorekit.models.ItineraryItemType;
import com.guestlogix.travelercorekit.models.Range;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ItineraryFilters implements Serializable {
    private Range<Date> dateRange;
    private ArrayList<ItineraryItemType> selectedProductTypes;

    public ItineraryFilters(Range<Date> dateRange, ArrayList<ItineraryItemType> selectedProductTypes) {
        this.dateRange = dateRange;
        this.selectedProductTypes = selectedProductTypes;
    }

    public Range<Date> getDateRange() {
        return dateRange;
    }

    public void setDateRange(Range<Date> dateRange) {
        this.dateRange = dateRange;
    }

    public ArrayList<ItineraryItemType> getSelectedItineraryItemTypes() {
        return selectedProductTypes;
    }

    public void setSelectedProductTypes(ArrayList<ItineraryItemType> selectedProductTypes) {
        this.selectedProductTypes = selectedProductTypes;
    }
}
