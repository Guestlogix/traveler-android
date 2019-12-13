package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.net.URL;

public interface CatalogItem extends Serializable {
    String getTitle();
    String getSubtitle();
    URL getImageUrl();
}