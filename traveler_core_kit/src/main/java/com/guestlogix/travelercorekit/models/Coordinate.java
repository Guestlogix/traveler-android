package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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
        public Coordinate instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            Double latitude = jsonObject.getDouble("latitude");
            Double longitude = jsonObject.getDouble("longitude");

            Assertion.eval(latitude != null);
            Assertion.eval(longitude != null);

            return new Coordinate(latitude, longitude);
        }
    }
}
