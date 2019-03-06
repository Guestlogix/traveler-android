package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.BookingContext;

public interface CheckAvailabilityCallback {
    void onAvailabilitySuccess(BookingContext bookingContext);
    void onAvailabilityError(Error error);
}
