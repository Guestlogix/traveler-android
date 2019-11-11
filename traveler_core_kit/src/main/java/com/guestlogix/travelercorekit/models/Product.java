package com.guestlogix.travelercorekit.models;

import java.io.Serializable;
import java.util.List;

public interface Product extends Serializable {
    String getId();
    String getTitle();
    Price getPrice();
    ProductType getProductType();
}
