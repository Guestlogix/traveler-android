package com.guestlogix.travelercorekit.callbacks;

public interface EmailOrderConfirmationCallback {
    void onEmailSuccess();
    void onEmailError(Error error);
}
