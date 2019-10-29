package com.guestlogix.travelercorekit.callbacks;

public interface PaymentConfirmationCallback {
    void onPaymentConfirmationSuccess();
    void onPaymentConfirmationError(Error error);
}
