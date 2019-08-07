package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.CatalogItemDetails;

public interface CatalogItemDetailsCallback {
    void onCatalogItemDetailsSuccess(CatalogItemDetails details);
    void onCatalogItemDetailsError(Error error);
}
