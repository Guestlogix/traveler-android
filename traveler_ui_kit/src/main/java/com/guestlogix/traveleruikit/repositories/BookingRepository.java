package com.guestlogix.traveleruikit.repositories;

import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.FetchPassesCallback;
import com.guestlogix.travelercorekit.models.BookingContext;

public class BookingRepository {
    public void fetchPasses(BookingContext bookingContext, FetchPassesCallback fetchPassesCallback) {
        Traveler.fetchPasses(bookingContext, fetchPassesCallback);
    }
}
