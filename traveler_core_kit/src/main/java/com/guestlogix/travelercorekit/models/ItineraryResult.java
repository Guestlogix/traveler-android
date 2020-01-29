package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ItineraryResult implements Serializable {
    private Date fromDAte;
    private Date toDate;
    private ArrayList<ItineraryItem> items;

    public ItineraryResult(Date fromDAte, Date toDate, ArrayList<ItineraryItem> items) {
        this.fromDAte = fromDAte;
        this.toDate = toDate;
        this.items = items;
    }

    public Date getFromDAte() {
        return fromDAte;
    }

    public Date getToDate() {
        return toDate;
    }

    public ArrayList<ItineraryItem> getItems() {
        return items;
    }

    static class ItineraryResultObjectMappingFactory implements ObjectMappingFactory<ItineraryResult> {
        @Override
        public ItineraryResult instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            Date fromDate = null;
            if (!jsonObject.isNull("fromDate"))
                fromDate = DateHelper.parseDate(jsonObject.getString("fromDate"));

            Date toDate = null;
            if (!jsonObject.isNull("toDate"))
                toDate = DateHelper.parseDate(jsonObject.getString("toDate"));

            ArrayList<ItineraryItem> items = new ArrayMappingFactory<ItineraryItem>(new ItineraryItem.ItineraryItemObjectMappingFactory()).instantiate(jsonObject.getJSONArray("items").toString());

            return new ItineraryResult(fromDate, toDate, items);
        }
    }
}
