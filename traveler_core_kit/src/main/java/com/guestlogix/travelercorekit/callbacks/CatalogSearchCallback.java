package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.models.Catalog;

public interface CatalogSearchCallback {
    void onCatalogSearchSuccess(Catalog catalog);

    void onCatalogSearchError(TravelerError error);
}
