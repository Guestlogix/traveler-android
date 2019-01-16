package com.guestlogix.traveler.repositories;

import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.models.CatalogQuery;

import java.util.List;

public class CatalogSearchRepository {
    public void catalogSearch(CatalogQuery catalogQuery, CatalogSearchCallback callback) {
        Traveler.fetchCatalog(catalogQuery, callback);
    }
}
