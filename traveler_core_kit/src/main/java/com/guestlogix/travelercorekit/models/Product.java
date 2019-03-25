package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public interface Product extends Serializable {
    String getId();
    Price getPrice();
}
