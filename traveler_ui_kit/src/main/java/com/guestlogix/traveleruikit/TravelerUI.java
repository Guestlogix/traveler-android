package com.guestlogix.traveleruikit;

import android.util.Log;
import com.guestlogix.travelercorekit.models.Currency;
import com.guestlogix.travelercorekit.models.PaymentManager;

public class TravelerUI {
    private static final String TAG = "TravelerUI";
    private static TravelerUI localInstance;

    private PaymentProvider mPaymentProvider;
    private Currency preferredCurrency;
    private PaymentManager paymentManager;

    private TravelerUI(PaymentProvider paymentProvider, PaymentManager manager, Currency preferredCurrency) {
        mPaymentProvider = paymentProvider;
        this.preferredCurrency = preferredCurrency;
        this.paymentManager = manager;
    }

    public static void initialize(PaymentProvider paymentProvider, PaymentManager manager) {
        initialize(paymentProvider, manager, Currency.USD);
    }

    public static void initialize(PaymentProvider paymentProvider, PaymentManager manager, Currency preferredCurrency) {
        if (localInstance != null) {
            Log.e(TAG, "SDK already initialized");
        } else {
            localInstance = new TravelerUI(paymentProvider, manager, preferredCurrency);
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

    public static PaymentManager getPaymentManager() {
        if (localInstance == null) {
            Log.e("TravelerUI", "SDK not initialized");
            return null;
        }

        return localInstance.paymentManager;
    }

    public static void setPreferredCurrency(Currency currency) {
        if (localInstance == null) {
            Log.e(TAG, "SDK not initialized");
            return;
        }

        localInstance.preferredCurrency = currency;
    }
}
