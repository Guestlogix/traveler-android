package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Order;

public interface CancellationCallback {
    void onCancellationSuccess(Order order);
    void onCancellationError(Error error);
}
