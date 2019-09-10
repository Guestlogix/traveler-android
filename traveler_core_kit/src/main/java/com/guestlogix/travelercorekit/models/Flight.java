package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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
        public Flight instantiate(JsonReader reader) throws Exception {
            String id = null;
            String number = null;
            Airport origin = null;
            Airport destination = null;
            String departureDateString = null;
            String arrivalDateString = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "flightNumber":
                        number = reader.nextString();
                        break;
                    case "origin":
                        origin = new Airport.AirportObjectMappingFactory().instantiate(reader);
                        break;
                    case "destination":
                        destination = new Airport.AirportObjectMappingFactory().instantiate(reader);
                        break;
                    case "departureTime":
                        departureDateString = reader.nextString();
                        break;
                    case "arrivalTime":
                        arrivalDateString = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

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
