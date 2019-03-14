package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Availability;

import java.util.List;

public interface FetchAvailabilitiesCallback {
    void onAvailabilitySuccess(List<Availability> availabilities);
    void onAvailabilityError(Error error);
}
