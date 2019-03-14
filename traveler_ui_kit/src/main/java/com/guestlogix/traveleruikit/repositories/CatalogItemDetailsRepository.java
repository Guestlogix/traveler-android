package com.guestlogix.traveleruikit.repositories;

import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.models.CatalogItem;

public class CatalogItemDetailsRepository {

    public void fetchDetails(CatalogItem catalogItem, CatalogItemDetailsCallback catalogItemDetailsCallback) {
        Traveler.fetchCatalogItemDetails(catalogItem, catalogItemDetailsCallback);
    }
}
