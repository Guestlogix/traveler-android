package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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
         * @param reader Object to parse from.
         * @return Flight model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public Flight instantiate(JsonReader reader) throws ObjectMappingException {
            try {
                String id = "";
                String number = "";
                Airport origin = null;
                Airport destination = null;
                Date departure = null;
                Date arrival = null;

                reader.beginObject();

                while (reader.hasNext()) {
                    String name = reader.nextName();

                    switch (name) {
                        case "id":
                            id = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "flightNumber":
                            number = JsonReaderHelper.readString(reader);
                            break;
                        case "origin":
                            origin = new Airport.AirportObjectMappingFactory().instantiate(reader);
                            break;
                        case "destination":
                            destination = new Airport.AirportObjectMappingFactory().instantiate(reader);
                            break;
                        case "departureTime":
                            departure = DateHelper.parseISO8601(JsonReaderHelper.readString(reader));
                            break;
                        case "arrivalTime":
                            arrival = DateHelper.parseISO8601(JsonReaderHelper.readString(reader));
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }

                reader.endObject();

                return new Flight(id, number, origin, destination, departure, arrival);

            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, e.getMessage()));
            } catch (ParseException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, e.getMessage()));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
