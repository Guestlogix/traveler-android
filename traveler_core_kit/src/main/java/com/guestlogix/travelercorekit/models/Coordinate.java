package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private Double latitude;
    private Double longitude;

    public Coordinate(Double latitude, Double longitude) {
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
            Double latitude = null;
            Double longitude = null;

            //TODO: fix this. we should not let null here. we should change the whole AnyItemMapping and its usecase to fix this.
            try {
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

            } catch (IllegalStateException ex) {
                reader.skipValue();
                return null;
            }

            if (latitude == null && longitude == null) {
                return null;
            }

            return new Coordinate(latitude, longitude);
        }
    }
}
