package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.Date;

public class ParkingSearchParameters implements Serializable {
    private String airportIATA;
    private Range<Date> dateRange;
    private BoundingBox boundingBox;

    private ParkingSearchParameters(
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


    public static class ParkingSearchParametersObjectMappingFactory implements ObjectMappingFactory<ParkingSearchParameters> {
        @Override
        public ParkingSearchParameters instantiate(JsonReader reader) throws Exception {
            String airportIATA = null;
            String startTime = null;
            String endTime = null;
            double topLeftLatitude = 0;
            double topLeftLongitude = 0;
            double bottomRightLatitude = 0;
            double bottomRightLongitude = 0;
            BoundingBox boundingBox = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "airport":
                        airportIATA = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "topLeftLatitude":
                        topLeftLatitude = reader.nextDouble();
                        break;
                    case "topLeftLongitude":
                        topLeftLongitude = reader.nextDouble();
                        break;
                    case "bottomRightLatitude":
                        bottomRightLatitude = reader.nextDouble();
                        break;
                    case "bottomRightLongitude":
                        bottomRightLongitude = reader.nextDouble();
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

            Coordinate topLeftCoordinate = new Coordinate(topLeftLatitude, topLeftLongitude);
            Coordinate bottomRightCoordinate = new Coordinate(bottomRightLatitude, bottomRightLongitude);
            boundingBox = new BoundingBox(topLeftCoordinate, bottomRightCoordinate);

            return new ParkingSearchParameters(airportIATA, dateRange, boundingBox);
        }
    }
}
