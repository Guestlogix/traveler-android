package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.ParkingItemDetails;

public interface ParkingItemDetailsCallback {
    void onParkingItemDetailsSuccess(ParkingItemDetails details);

    void onParkingItemDetailsError(Error error);
}
