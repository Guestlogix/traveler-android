package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Group;

import java.util.List;

public interface CatalogSearchCallback {

    void onCatalogSearchSuccess(List<Group> groups);
    void onCatalogSearchError(TravelerError error);
}
