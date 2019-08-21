package com.guestlogix.travelercorekit.flight.helpers;

import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.List;

public class FlightSearchCallbackSpy implements FlightSearchCallback {
    int onFlightSearchSuccess_invocationCount = 0;
    int onFlightSearchError_invocationCount = 0;

    @Override
    public void onFlightSearchSuccess(List<Flight> flights) {
        System.out.println("SPY: onFlightSearchSuccess called!");
        onFlightSearchSuccess_invocationCount++;
    }

    @Override
    public void onFlightSearchError(Error error) {
        System.out.println("SPY: onFlightSearchError called!");
        onFlightSearchError_invocationCount++;
    }

    public int getOnFlightSearchSuccess_invocationCount() {
        return onFlightSearchSuccess_invocationCount;
    }

    public int getOnFlightSearchError_invocationCount() {
        return onFlightSearchError_invocationCount;
    }

}
