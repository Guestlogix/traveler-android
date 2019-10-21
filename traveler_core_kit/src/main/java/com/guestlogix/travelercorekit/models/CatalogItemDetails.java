package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

public interface CatalogItemDetails extends Serializable {
    String getTitle();
    String getDescription();
    List<Attribute> getInformation();
    List<URL> getImageURLs();
    List<Location> getLocations();
    ContactInfo getContact();
    Supplier getSupplier();
    String getDisclaimer();
    String getTermsAndConditions();
    ProductType getProductType();
}
