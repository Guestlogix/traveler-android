package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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
        public Location instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String address = jsonObject.getString("address");
            Double latitude = jsonObject.getDouble("latitude");
            Double longitude = jsonObject.getDouble("longitude");


            Assertion.eval(address != null);
            Assertion.eval(latitude != null);
            Assertion.eval(longitude != null);

            return new Location(address, latitude, longitude);
        }
    }

}

