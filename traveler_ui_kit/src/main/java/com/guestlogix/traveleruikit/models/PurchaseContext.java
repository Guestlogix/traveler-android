package com.guestlogix.traveleruikit.models;

import com.guestlogix.travelercorekit.models.Price;

import java.io.Serializable;

public interface PurchaseContext extends Serializable {
    Price getPrice();

    State getState();

    enum State implements Serializable {
        DEFAULT, LOADING, AVAILABLE, NOT_AVAILABLE, OPTION_REQUIRED
    }
}
