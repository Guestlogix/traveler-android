package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public class ItineraryItem implements Serializable {
    private String id;
    private URL imageUrl;
    private String title;
    private String subtitle;
    private String orderId;
    private Date startDate;
    private Date endDate;
    private Integer duration;
    private Coordinate startLocation;
    private Coordinate endLocation;
    private Integer quantity;
    private ItineraryItemType itineraryItemType;
    private boolean isAllDay;

    public ItineraryItem(String id,
                         URL imageUrl,
                         String title,
                         String subtitle,
                         String orderId,
                         Date startDate,
                         Date endDate,
                         Integer duration,
                         Coordinate startLocation,
                         Coordinate endLocation,
                         Integer quantity,
                         ItineraryItemType itineraryItemType,
                         boolean isAllDay) {

        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.orderId = orderId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.quantity = quantity;
        this.itineraryItemType = itineraryItemType;
        this.isAllDay = isAllDay;
    }

    public String getId() {
        return id;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getOrderId() {
        return orderId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getDuration() {
        return duration;
    }

    public Coordinate getStartLocation() {
        return startLocation;
    }

    public Coordinate getEndLocation() {
        return endLocation;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItineraryItemType getItineraryItemType() {
        return itineraryItemType;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    static class ItineraryItemObjectMappingFactory implements ObjectMappingFactory<ItineraryItem> {
        @Override
        public ItineraryItem instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String id = jsonObject.getString("id");
            URL imageUrl = null;
            if (!jsonObject.isNull("iconUrl"))
                imageUrl = new URL(jsonObject.getString("iconUrl"));
            String title = jsonObject.getString("title");
            String subtitle = jsonObject.getString("subTitle");
            String orderId = jsonObject.getNullableString("orderId");

            Date startDate = DateHelper.parseDate(jsonObject.getString("startDateTime"));

            Date endDate = null;
            if (!jsonObject.isNull("endDateTime"))
                endDate = DateHelper.parseDate(jsonObject.getString("endDateTime"));

            Integer duration = jsonObject.getNullableInt("durationHours");

            Coordinate startLocation = null;
            if (!jsonObject.isNull("startLocation"))
                startLocation = new Coordinate.CoordinateObjectMappingFactory().instantiate(jsonObject.getJSONObject("startLocation").toString());

            Coordinate endLocation = null;
            if (!jsonObject.isNull("endLocation"))
                endLocation = new Coordinate.CoordinateObjectMappingFactory().instantiate(jsonObject.getJSONObject("endLocation").toString());

            Integer quantity = jsonObject.getNullableInt("reservationQuantity");
            ItineraryItemType itineraryItemType = ItineraryItemType.fromString(jsonObject.getString("type"));
            boolean isAllDay = jsonObject.getBoolean("isAllDay");


            return new ItineraryItem(id, imageUrl, title, subtitle, orderId, startDate, endDate, duration, startLocation, endLocation, quantity, itineraryItemType, isAllDay);
        }
    }
}
