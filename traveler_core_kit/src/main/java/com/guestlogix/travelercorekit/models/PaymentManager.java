package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.callbacks.PaymentSaveCallback;
import com.guestlogix.travelercorekit.callbacks.PaymentsFetchCallback;

public interface PaymentManager {
    void fetchPayments(PaymentsFetchCallback callback);
    void savePayment(Payment payment, PaymentSaveCallback callback);
}
