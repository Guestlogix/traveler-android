package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.util.Date;

public class ParkingItemSearchParameters implements Serializable {
    private String airportIATA;
    private Range<Date> dateRange;
    private BoundingBox boundingBox;

    private ParkingItemSearchParameters(
            String airportIATA,
            Range<Date> dateRange,
            BoundingBox boundingBox) {
        this.airportIATA = airportIATA;
        this.dateRange = dateRange;
        this.boundingBox = boundingBox;
    }


    public String getAirportIATA() {
        return airportIATA;
    }

    public Range<Date> getDateRange() {
        return dateRange;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public static class ParkingItemSearchParametersObjectMappingFactory implements ObjectMappingFactory<ParkingItemSearchParameters> {
        @Override
        public ParkingItemSearchParameters instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String airportIATA = jsonObject.getNullableString("airport");
            String startTime = jsonObject.getString("startTime");
            String endTime = jsonObject.getString("endTime");
            Double topLeftLatitude = jsonObject.getNullableDouble("topLeftLatitude");
            Double topLeftLongitude = jsonObject.getNullableDouble("topLeftLongitude");
            Double bottomRightLatitude = jsonObject.getNullableDouble("bottomRightLatitude");
            Double bottomRightLongitude = jsonObject.getNullableDouble("bottomRightLongitude");

            Range<Date> dateRange = new Range<>(DateHelper.parseISO8601(startTime), DateHelper.parseISO8601(endTime));

            BoundingBox boundingBox = null;
            if (topLeftLatitude != null && topLeftLongitude != null &&
                    bottomRightLatitude != null && bottomRightLongitude != null) {
                Coordinate topLeftCoordinate = new Coordinate(topLeftLatitude, topLeftLongitude);
                Coordinate bottomRightCoordinate = new Coordinate(bottomRightLatitude, bottomRightLongitude);
                boundingBox = new BoundingBox(topLeftCoordinate, bottomRightCoordinate);
            }

            return new ParkingItemSearchParameters(airportIATA, dateRange, boundingBox);
        }
    }
}
