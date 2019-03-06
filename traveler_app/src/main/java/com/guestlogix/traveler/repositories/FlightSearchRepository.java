package com.guestlogix.traveler.repositories;

import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.FlightQuery;

public class FlightSearchRepository {
    public void flightSearch(FlightQuery flightSearchQuery, FlightSearchCallback flightSearchCallback) {
        Traveler.flightSearch(flightSearchQuery, flightSearchCallback);
    }
}
