package com.guestlogix.traveleruikit.repositories;

import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.callbacks.CheckAvailabilityCallback;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.FlightQuery;

public class CatalogItemDetailsRepository {

    public void fetchDetails(CatalogItem catalogItem, CatalogItemDetailsCallback catalogItemDetailsCallback) {
        Traveler.fetchCatalogItemDetails(catalogItem, catalogItemDetailsCallback);
    }

    public void fetchAvailability(BookingContext bookingContext, CheckAvailabilityCallback checkAvailabilityCallback) {
        Traveler.checkAvailability(bookingContext, checkAvailabilityCallback);
    }
}
