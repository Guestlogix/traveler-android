package com.guestlogix.travelercorekit.models;

public class Airport {
    String name;
    String code;
    String city;

    public Airport(String name, String code, String city) {
        this.name = name;
        this.code = code;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCity() {
        return city;
    }
}
