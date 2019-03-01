package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Availability;

import java.util.List;

public interface CheckAvailabilityCallback {
    void onAvailabilitySuccess(List<Availability> availability);
    void onAvailabilityError(Error error);
}
