package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Payment;
import com.guestlogix.travelercorekit.models.Receipt;

public interface ProcessOrderCallback {
    void onOrderProcessSuccess(Receipt receipt);
    void onOrderProcessError(Error error);
}
