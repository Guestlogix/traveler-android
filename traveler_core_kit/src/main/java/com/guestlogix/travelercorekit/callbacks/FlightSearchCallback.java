package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;
import java.util.List;

public interface FlightSearchCallback {
    void onFlightSearchSuccess(List<Flight> flights);
    void onFlightSearchError(TravelerError error);
}
