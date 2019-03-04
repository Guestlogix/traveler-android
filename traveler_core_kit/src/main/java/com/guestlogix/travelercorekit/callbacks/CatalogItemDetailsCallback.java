package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.CatalogItemDetails;

public interface CatalogItemDetailsCallback {
    void onCatalogItemDetailsSuccess(CatalogItemDetails catalog);
    void onCatalogItemDetailsError(Error error);
}
