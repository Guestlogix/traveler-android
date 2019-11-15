package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Payment;

import java.util.List;

public interface PaymentsFetchCallback {
    void onPaymentsFetchSuccess(List<Payment> paymentList);
    void onPaymentsFetchError(Error error);
}
