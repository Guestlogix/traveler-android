package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Flight;

import java.util.List;

public interface FlightSearchCallback {
    void onFlightSearchSuccess(List<Flight> flights);
    void onFlightSearchError(Error error);
}
