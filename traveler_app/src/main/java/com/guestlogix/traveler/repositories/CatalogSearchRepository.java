package com.guestlogix.traveler.repositories;

import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;

import java.util.List;

public class CatalogSearchRepository {
    public void catalogSearch(List<String> flightIds, CatalogSearchCallback callback) {
        Traveler.catalogSearch(flightIds, callback);
    }
}
