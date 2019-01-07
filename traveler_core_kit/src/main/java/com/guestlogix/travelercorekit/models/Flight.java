package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.Log;
import com.guestlogix.travelercorekit.network.MappingException;
import com.guestlogix.travelercorekit.network.MappingFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Flight {
    String id;
    String number;
    Airport departureAirport;
    Airport arrivalAirport;
    Date departureDate;
    Date arrivalDate;

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

    public static class FlightMappingFactory implements MappingFactory<Flight> {
        private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
        private static final String TAG = "FlightMappingFactory";

        @Override
        public Flight instantiate(JsonReader reader) throws MappingException {
            try {
                return readFlight(reader);
            } catch (ParseException e) {
                Log.e(TAG, "Date parse error occurred");
                throw new MappingException();
            } catch (IOException e) {
                Log.e(TAG, "Parsing exception occurred");
                throw new MappingException();
            }
        }

        private Flight readFlight(JsonReader reader) throws IOException, ParseException {
            String id = "";
            String number = "";
            Airport origin = null;
            Airport destination = null;
            Date departure = null;
            Date arrival = null;

            reader.beginObject();
            String s;

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "flightNumber":
                        number = reader.nextString();
                        break;
                    case "origin":
                        origin = readAirport(reader);
                        break;
                    case "destination":
                        destination = readAirport(reader);
                        break;
                    case "departureTime":
                        departure = new SimpleDateFormat(DATE_PATTERN).parse(reader.nextString());
                        break;
                    case "arrivalTime":
                        departure = new SimpleDateFormat(DATE_PATTERN).parse(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            return new Flight(id, number, origin, destination, departure, arrival);
        }

        private Airport readAirport (JsonReader reader) throws IOException {
            String name = "";
            String code = "";
            String city = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "name":
                        name = reader.nextString();
                        break;
                    case "iata":
                        code = reader.nextString();
                        break;
                    case "city":
                        city = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();
            return new Airport(name, code, city);
        }
    }
}
