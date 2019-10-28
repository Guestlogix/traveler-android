package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.BookingItemSearchResult;

public interface BookingSearchCallback {
    void onBookingSearchSuccess(BookingItemSearchResult searchResult);
    void onBookingSearchError(Error error);
}
