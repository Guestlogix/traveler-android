package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.net.URL;

public interface CatalogItem<T> extends Serializable {
    String getTitle();
    String getSubtitle();
    URL getImageUrl();
    T getItemResource();
}