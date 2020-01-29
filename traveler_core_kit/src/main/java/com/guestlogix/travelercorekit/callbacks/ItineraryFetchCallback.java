package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.ItineraryResult;

public interface ItineraryFetchCallback {
    /**
     * called when partner offering is fetched successfully
     * @param itineraryResult is the itinerary result of the query
     */
    void onSuccess(ItineraryResult itineraryResult);
    /**
     * called when partner offering could not be fetched because of a problem
     * @param error is the problem details
     */
    void onError(Error error);
}
