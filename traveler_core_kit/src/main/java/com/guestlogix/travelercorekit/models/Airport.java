package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

public class Airport implements Serializable {
    private String code;
    private String name;
    private String city;
    private TimeZone timeZone;

    private Airport(@NonNull String name, @NonNull String code, @NonNull String city, @NonNull TimeZone timeZone) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.timeZone = timeZone;
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

    public TimeZone getTimeZone() {
        return timeZone;
    }

    static class AirportObjectMappingFactory implements ObjectMappingFactory<Airport> {
        @Override
        public Airport instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String name = jsonObject.getString("name");
            String code = jsonObject.getString("iata");
            String city = jsonObject.getString("city");


            // Java 7 and below can only use custom ids for timezones or specific identifiers
            // like 'America/NewYork'
            // Custom ids are of the format `GMT(+-)HHmm`
            // Since the API is returning a double representing the hour offset for that specific
            // timezone, below we translate that value into a custom id so we can accurately generate
            // a TimeZone object for the Airport.
            // TimeZone for Airport is valuable because it allows flight times to be presented in the
            // local time of the respective airports.

            double offsetInHours = jsonObject.getDouble("utcOffsetHours");
            int sign = (int) Math.signum(offsetInHours);
            int offsetInMinutes = (int) Math.abs(offsetInHours * 60);
            int hoursPortion = offsetInMinutes / 60;
            int minutesPortion = offsetInMinutes % 60;
            int offsetValue = sign * ((hoursPortion * 100) + minutesPortion);
            String customID = String.format(Locale.US, "GMT%+d", offsetValue);
            TimeZone timeZone = TimeZone.getTimeZone(customID);


            Assertion.eval(name != null);
            Assertion.eval(code != null);
            Assertion.eval(city != null);
            Assertion.eval(timeZone != null);

            return new Airport(name, code, city, timeZone);
        }
    }
}
