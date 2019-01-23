package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;

public interface CatalogItemDetailsCallback {
    void onCatalogItemDetailsSuccess(CatalogItemDetails catalog);
    void onCatalogItemDetailsError(TravelerError error);
}
