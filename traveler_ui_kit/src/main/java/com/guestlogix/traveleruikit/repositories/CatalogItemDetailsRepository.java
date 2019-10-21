package com.guestlogix.traveleruikit.repositories;

import com.guestlogix.travelercorekit.callbacks.BookingItemDetailsCallback;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;

public class CatalogItemDetailsRepository {

    public void fetchDetails(Product product, BookingItemDetailsCallback productDetailsCallback) {
        Traveler.fetchProductDetails(product, productDetailsCallback);
    }
}
