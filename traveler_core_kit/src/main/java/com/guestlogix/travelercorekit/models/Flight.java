package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Flight implements Serializable {
    private String id;
    private String number;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Date departureDate;
    private Date arrivalDate;

    private Flight(@NonNull String id, @NonNull String number, @NonNull Airport departureAirport, @NonNull Airport arrivalAirport, @NonNull Date departureDate, @NonNull Date arrivalDate) {
        this.id = id;
        this.number = number;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public String getDepartureDateDescription(SimpleDateFormat format) {
        format.setTimeZone(departureAirport.getTimeZone());
        return format.format(departureDate);
    }

    public String getArrivalDateDescription(SimpleDateFormat format) {
        format.setTimeZone(arrivalAirport.getTimeZone());
        return format.format(arrivalDate);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Flight)) {
            return false;
        }

        return ((Flight) obj).id.equals(this.id);
    }

    static class FlightObjectMappingFactory implements ObjectMappingFactory<Flight> {
        @Override
        public Flight instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String number = jsonObject.getString("flightNumber");
            Airport origin = new Airport.AirportObjectMappingFactory().instantiate(jsonObject.getJSONObject("origin").toString());
            Airport destination = new Airport.AirportObjectMappingFactory().instantiate(jsonObject.getJSONObject("destination").toString());
            String departureDateString = jsonObject.getString("departureTime");
            String arrivalDateString = jsonObject.getString("arrivalTime");

            Assertion.eval(origin != null);
            Assertion.eval(destination != null);

            Date departure = DateHelper.parseISO8601(departureDateString, origin.getTimeZone());
            Date arrival = DateHelper.parseISO8601(arrivalDateString, destination.getTimeZone());

            Assertion.eval(id != null);
            Assertion.eval(number != null);
            Assertion.eval(departure != null);
            Assertion.eval(arrival != null);

            return new Flight(id, number, origin, destination, departure, arrival);
        }
    }
}
