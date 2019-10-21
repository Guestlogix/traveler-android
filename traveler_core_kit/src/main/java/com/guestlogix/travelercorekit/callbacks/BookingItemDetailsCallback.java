package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.CatalogItemDetails;

public interface BookingItemDetailsCallback {
    void onBookingItemDetailsSuccess(CatalogItemDetails details);
    void onBookingItemDetailsError(Error error);
}
