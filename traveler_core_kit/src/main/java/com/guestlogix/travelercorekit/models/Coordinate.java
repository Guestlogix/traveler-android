package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private double latitude;
    private double longitude;

    public Coordinate(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    static class CoordinateObjectMappingFactory implements ObjectMappingFactory<Coordinate> {

        @Override
        public Coordinate instantiate(JsonReader reader) throws Exception {
            double latitude = 0;
            double longitude = 0;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "latitude":
                        latitude = reader.nextDouble();
                        break;
                    case "longitude":
                        longitude = reader.nextDouble();
                        break;
                }
            }

            reader.endObject();

            return new Coordinate(latitude, longitude);
        }
    }
}
