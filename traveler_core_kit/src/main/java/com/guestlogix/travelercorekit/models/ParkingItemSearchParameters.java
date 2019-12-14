package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
        public ParkingItemSearchParameters instantiate(JsonReader reader) throws Exception {
            String airportIATA = null;
            String startTime = null;
            String endTime = null;
            Double topLeftLatitude = null;
            Double topLeftLongitude = null;
            Double bottomRightLatitude = null;
            Double bottomRightLongitude = null;
            BoundingBox boundingBox = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "airport":
                        airportIATA = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "topLeftLatitude":
                        topLeftLatitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "topLeftLongitude":
                        topLeftLongitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "bottomRightLatitude":
                        bottomRightLatitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "bottomRightLongitude":
                        bottomRightLongitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "startTime":
                        startTime = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "endTime":
                        endTime = JsonReaderHelper.nextNullableString(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Range<Date> dateRange = new Range<>(DateHelper.parseISO8601(startTime), DateHelper.parseISO8601(endTime));

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
