package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class Location implements Serializable {
    private String address;
    private double latitude;
    private double longitude;

    private Location(String address, Double latitude, Double longitude) throws ObjectMappingException {
        this.address = address;
        if (null == latitude) {
            throw new IllegalArgumentException("latitude can not be empty");
        } else {
            this.latitude = latitude;
        }
        if (longitude == null) {
            throw new IllegalArgumentException("longitude can not be empty");
        } else {
            this.longitude = longitude;
        }
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    static class LocationObjectMappingFactory implements ObjectMappingFactory<Location> {
        /**
         * Parses a reader object into Location model.
         *
         * @param reader object to parse from.
         * @return Location model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or if the required field is missing.
         */
        @Override
        public Location instantiate(JsonReader reader) throws Exception {
            String address = "";
            Double latitude = null;
            Double longitude = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "address":
                        address = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "latitude":
                        latitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "longitude":
                        longitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != latitude, "latitude can not be empty");
            Assertion.eval(null != longitude, "longitude can not be empty");

            return new Location(address, latitude, longitude);
        }
    }
}

