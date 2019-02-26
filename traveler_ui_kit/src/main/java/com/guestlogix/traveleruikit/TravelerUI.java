package com.guestlogix.traveleruikit;

import com.guestlogix.travelercorekit.utilities.TravelerLog;

public class TravelerUI {
    private static TravelerUI localInstance;

    private PaymentProvider mPaymentProvider;

    public static void initialize(PaymentProvider paymentProvider) {
        if (localInstance != null) {
            TravelerLog.e("SDK already initialized");
        } else {
            localInstance = new TravelerUI(paymentProvider);
        }
    }

    private TravelerUI(PaymentProvider paymentProvider) {
        mPaymentProvider = paymentProvider;
    }

    static PaymentProvider getPaymentProvider() {
        if (localInstance == null) {
            TravelerLog.e("SDK not initialized");
            return null;
        }

        return localInstance.mPaymentProvider;
    }
}
