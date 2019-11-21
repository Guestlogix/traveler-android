package com.guestlogix.traveleruikit;

import android.util.Log;

import com.guestlogix.travelercorekit.models.Currency;

public class TravelerUI {
    private static final String TAG = "TravelerUI";
    private static TravelerUI localInstance;

    private PaymentProvider mPaymentProvider;
    private Currency preferredCurrency;

    private TravelerUI(PaymentProvider paymentProvider, Currency preferredCurrency) {
        mPaymentProvider = paymentProvider;
        this.preferredCurrency = preferredCurrency;
    }

    public static void initialize(PaymentProvider paymentProvider) {
        initialize(paymentProvider, Currency.USD);
    }

    public static void initialize(PaymentProvider paymentProvider, Currency preferredCurrency) {
        if (localInstance != null) {
            Log.e(TAG, "SDK already initialized");
        } else {
            localInstance = new TravelerUI(paymentProvider, preferredCurrency);
        }
    }

    public static PaymentProvider getPaymentProvider() {
        if (localInstance == null) {
            Log.e(TAG, "SDK not initialized");
            return null;
        }

        return localInstance.mPaymentProvider;
    }

    public static Currency getPreferredCurrency() {
        if (localInstance == null) {
            Log.e(TAG, "SDK not initialized");
            return null;
        }

        return localInstance.preferredCurrency;
    }

    public static void setPreferredCurrency(Currency currency) {
        if (localInstance == null) {
            Log.e(TAG, "SDK not initialized");
            return;
        }

        localInstance.preferredCurrency = currency;
    }
}
