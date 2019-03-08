package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import com.guestlogix.traveler_annotations.TravelerApiModel;

import java.io.Serializable;

@TravelerApiModel
public class Airport implements Serializable {
    private String iata;
    private String name;
    private String city;

    public Airport(@NonNull String code, @NonNull String name, @NonNull String city) {
        this.name = name;
        this.iata = code;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return iata;
    }

    public String getCity() {
        return city;
    }
}
