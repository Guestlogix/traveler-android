package com.guestlogix.travelercorekit.callbacks;

public interface PaymentSaveCallback {
    void onPaymentSaveSuccess();
    void onPaymentSaveError(Error error);
}
