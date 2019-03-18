package com.guestlogix.travelercorekit.callbacks;

public interface OrderCreateCallback {
    void onOrderCreateSuccess(); // TODO

    void onOrderCreateFailure(Error error);
}
