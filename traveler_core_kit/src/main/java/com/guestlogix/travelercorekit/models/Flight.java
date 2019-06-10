package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.Date;

public class Flight implements Serializable {
    private String id;
    private String number;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Date departureDate;
    private Date arrivalDate;

    private Flight(String id, String number, Airport departureAirport, Airport arrivalAirport, Date departureDate, Date arrivalDate) throws IllegalArgumentException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id can not be empty");
        } else {
            this.id = id;
        }
        this.number = number;
        if (departureAirport == null) {
            throw new IllegalArgumentException("departureAirport can not be null");
        } else {
            this.departureAirport = departureAirport;
        }
        if (arrivalAirport == null) {
            throw new IllegalArgumentException("arrivalAirport can not be null");
        } else {
            this.arrivalAirport = arrivalAirport;
        }
        if (departureDate == null) {
            throw new IllegalArgumentException("departureDate can not be null");
        } else {
            this.departureDate = departureDate;
        }
        if (arrivalDate == null) {
            throw new IllegalArgumentException("arrivalDate can not be null");
        } else {
            this.arrivalDate = arrivalDate;
        }
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

    /**
     * Factory class to construct Flight model from {@code JsonReader}.
     */
    static class FlightObjectMappingFactory implements ObjectMappingFactory<Flight> {
        /**
         * Parses a reader object into Flight model.
         *
         * @param reader object to parse from.
         * @return Flight model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type or missing required field.
         */
        @Override
        public Flight instantiate(JsonReader reader) throws Exception {
            String key;
            String id = "";
            String number = "";
            Airport origin = null;
            Airport destination = null;
            Date departure = null;
            Date arrival = null;

            reader.beginObject();

            while (reader.hasNext()) {
                key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "flightNumber":
                        number = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "origin":
                        if (reader.peek() != JsonToken.NULL) {
                            origin = new Airport.AirportObjectMappingFactory().instantiate(reader);
                        } else {
                            origin = null;
                            reader.skipValue();
                        }
                        break;
                    case "destination":
                        if (reader.peek() != JsonToken.NULL) {
                            destination = new Airport.AirportObjectMappingFactory().instantiate(reader);
                        } else {
                            destination = null;
                            reader.skipValue();
                        }
                        break;
                    case "departureTime":
                        departure = DateHelper.parseISO8601(reader.nextString());
                        break;
                    case "arrivalTime":
                        arrival = DateHelper.parseISO8601(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(null != id && !id.trim().isEmpty());
            Assertion.eval(null != origin, "origin can not be null");
            Assertion.eval(null != destination, "destination can not be null");
            Assertion.eval(null != departure, "departure can not be null");
            Assertion.eval(null != arrival, "arrival can not be null");

            return new Flight(id, number, origin, destination, departure, arrival);
        }
    }
}
