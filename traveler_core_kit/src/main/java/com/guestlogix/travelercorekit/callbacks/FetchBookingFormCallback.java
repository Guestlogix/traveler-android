package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.BookingForm;

public interface FetchBookingFormCallback {
    void onBookingFormFetchSuccess(BookingForm bookingForm);
    void onBookingFormFetchError(Error error);
}
