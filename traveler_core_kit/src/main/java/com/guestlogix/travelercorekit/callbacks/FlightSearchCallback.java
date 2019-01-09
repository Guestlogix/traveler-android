package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;

public interface FlightSearchCallback {

    void onFlightSearchSuccess(ArrayList<Flight> flights);
    void onFlightSearchError(TravelerError error);

}
