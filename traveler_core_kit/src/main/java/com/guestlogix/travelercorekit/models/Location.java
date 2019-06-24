package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;

public class Location implements Serializable {
    private String address;
    private Double latitude;
    private Double longitude;

    private Location(@NonNull String address, @NonNull Double latitude, @NonNull Double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
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
        @Override
        public Location instantiate(JsonReader reader) throws Exception {
            String address = null;
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
                        latitude = reader.nextDouble();
                        break;
                    case "longitude":
                        longitude = reader.nextDouble();
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(address != null);
            Assertion.eval(latitude != null);
            Assertion.eval(longitude != null);

            return new Location(address, latitude, longitude);
        }
    }

}

