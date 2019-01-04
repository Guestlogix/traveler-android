package com.guestlogix.travelercorekit.task;

import android.util.JsonReader;
import android.util.Log;
import com.guestlogix.travelercorekit.models.Airport;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.network.AuthenticatedRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightFetchTask extends AuthenticatedNetworkRequestTask {

    List<Flight> mFlights;

    public FlightFetchTask(Session mSession, AuthenticatedRequest mRequest) {
        super(mSession, mRequest);

        mResponseHandler = ((stream) -> {
            try {
                parseFetchedFlights(stream);
                // Flights were successfully loaded.
            } catch (IOException e) {
                Log.e("Traveler", "An error has occurred");
            }
        });
    }

    private void parseFetchedFlights(InputStream stream) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(stream))){
            mFlights = readFlightsArray(reader);
        }
    }

    private List<Flight> readFlightsArray(JsonReader reader) throws IOException {
        List<Flight> flights = new ArrayList<>();

        reader.beginArray();

        while (reader.hasNext()) {
            flights.add(readFlight(reader));
        }

        return flights;
    }

    private Flight readFlight(JsonReader reader) throws IOException {
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
                    // TODO add date conversion
                    String s = reader.nextString();
                    //departure = new Date(s);
                    break;
                case "arrivalTime":
                    // TODO add date conversion
                    s = reader.nextString();
                    //arrival = new Date(reader.nextString());
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
