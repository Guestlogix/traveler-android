package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Catalog;

public interface catalogSearchCallback {
    void onCatalogSuccess(Catalog catalog);
    void onCatalogError(Error error);
}
