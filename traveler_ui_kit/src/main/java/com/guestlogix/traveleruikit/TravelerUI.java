package com.guestlogix.traveleruikit;

import android.util.Log;

public class TravelerUI {
    private static TravelerUI localInstance;
    private static final String TAG = "Traveler";

    private PaymentProvider mPaymentProvider;

    public static void initialize(PaymentProvider paymentProvider) {
        if (localInstance != null) {
            Log.e(TAG, "SDK already initialized");
        } else {
            localInstance = new TravelerUI(paymentProvider);
        }
    }

    private TravelerUI(PaymentProvider paymentProvider) {
        mPaymentProvider = paymentProvider;
    }

    static PaymentProvider getPaymentProvider() {
        if (localInstance == null) {
            Log.e(TAG, "SDK not initialized");
            return null;
        }

        return localInstance.mPaymentProvider;
    }
}
