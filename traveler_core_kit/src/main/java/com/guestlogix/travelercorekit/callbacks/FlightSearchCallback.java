package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;

public interface FlightSearchCallback {

    void onFlightSearchSuccess(ArrayList<Flight> flights);
    void onFlightSearchError(Error error);

}
