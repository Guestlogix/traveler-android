package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Availability;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;

public interface CheckAvailabilityCallback {

    void onCheckAvailabilitySuccess(Availability availability);

    void onCheckAvailabilityError(TravelerError error);
}
