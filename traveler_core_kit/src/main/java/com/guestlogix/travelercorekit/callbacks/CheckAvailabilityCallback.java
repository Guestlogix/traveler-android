package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.models.Availability;

import java.util.List;

public interface CheckAvailabilityCallback {
    void onCheckAvailabilitySuccess(List<Availability> availability);

    void onCheckAvailabilityError(TravelerError error);
}
