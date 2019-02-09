package com.guestlogix.traveleruikit.repositories;

import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.models.CatalogQuery;

public class CatalogWidgetRepository {
    public void catalogSearch(CatalogQuery catalogQuery, CatalogSearchCallback callback) {
        Traveler.fetchCatalog(catalogQuery, callback);
    }
}
