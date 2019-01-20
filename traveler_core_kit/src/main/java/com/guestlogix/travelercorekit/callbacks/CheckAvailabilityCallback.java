package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;

import java.util.ArrayList;

public interface CheckAvailabilityCallback {

    void onCheckAvailabilitySuccess(ArrayList<Availability> availability);

    void onCheckAvailabilityError(TravelerError error);
}
