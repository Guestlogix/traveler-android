package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.List;

public interface FlightSearchCallback {
    void onFlightSearchSuccess(List<Flight> flights);

    void onFlightSearchError(TravelerError error);
}
