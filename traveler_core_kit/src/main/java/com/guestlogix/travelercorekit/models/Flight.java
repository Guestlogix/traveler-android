package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.Log;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
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

    public Flight(String id, String number, Airport departureAirport, Airport arrivalAirport, Date departureDate, Date arrivalDate) {
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


    public static class FlightObjectMappingFactory implements ObjectMappingFactory<Flight> {
        private static final String TAG = "FlightMappingFactory";

        @Override
        public Flight instantiate(JsonReader reader) throws ObjectMappingException {
            try {
                return readFlight(reader);
            } catch (ParseException e) {
                Log.e(TAG, "Date parse error occurred");
                throw new ObjectMappingException(new TravelerError(TravelerErrorCode.PARSING_ERROR, "Could not convert date"));
            } catch (IOException e) {
                Log.e(TAG, "Parsing exception occurred");
                throw new ObjectMappingException(new TravelerError(TravelerErrorCode.PARSING_ERROR, "IOException has occurred"));
            }
        }
    }

    private static Flight readFlight(JsonReader reader) throws IOException, ParseException, ObjectMappingException {
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
                    id = JsonReaderHelper.readString(reader);
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
                    departure = DateHelper.getDateTimeAsObject(JsonReaderHelper.readString(reader));
                    break;
                case "arrivalTime":
                    arrival = DateHelper.getDateTimeAsObject(JsonReaderHelper.readString(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();

        return new Flight(id, number, origin, destination, departure, arrival);
    }
}
