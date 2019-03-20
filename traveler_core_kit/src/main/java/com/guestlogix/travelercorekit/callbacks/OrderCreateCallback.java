package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Order;

public interface OrderCreateCallback {
    void onOrderCreateSuccess(Order order);

    void onOrderCreateFailure(Error error);
}
