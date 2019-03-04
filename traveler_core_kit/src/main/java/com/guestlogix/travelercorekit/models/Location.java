package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;

public class Location implements Serializable {
    private String address;
    private double latitude;
    private double longitude;

    public Location(String address, double latitude, double longitude) {
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

    public static class LocationObjectMappingFactory implements ObjectMappingFactory<Location> {

        @Override
        public Location instantiate(JsonReader reader) throws IOException {
            return readItem(reader);
        }

        private Location readItem(JsonReader reader) throws IOException {
            String address = "";
            double latitude = 0.0;
            double longitude = 0.0;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "address":
                        address = JsonReaderHelper.readString(reader);
                        break;
                    case "latitude":
                        latitude = JsonReaderHelper.readDouble(reader);
                        break;
                    case "longitude":
                        longitude = JsonReaderHelper.readDouble(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new Location(address, latitude, longitude);
        }
    }
}
