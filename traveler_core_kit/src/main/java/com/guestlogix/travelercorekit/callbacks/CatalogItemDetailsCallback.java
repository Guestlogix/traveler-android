package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.CatalogItemDetails;


//TODO: rename to product item details callback. rename teh methods too
public interface CatalogItemDetailsCallback {
    void onCatalogItemDetailsSuccess(CatalogItemDetails details);
    void onCatalogItemDetailsError(Error error);
}
