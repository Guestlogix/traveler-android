package com.guestlogix.travelercorekit.callbacks;


import com.guestlogix.travelercorekit.models.ParkingItemSearchResult;

public interface ParkingSearchCallback {
    void onParkingSearchSuccess(ParkingItemSearchResult searchResult);
    void onParkingSearchError(Error error);
}
