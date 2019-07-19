package com.guestlogix.traveleruikit;

import android.content.Context;
import android.content.Intent;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.Currency;
import com.guestlogix.traveleruikit.activities.CatalogItemDetailsActivity;

public class TravelerUI {
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
            TravelerLog.e("SDK already initialized");
        } else {
            localInstance = new TravelerUI(paymentProvider, preferredCurrency);
        }
    }

    public static PaymentProvider getPaymentProvider() {
        if (localInstance == null) {
            TravelerLog.e("SDK not initialized");
            return null;
        }

        return localInstance.mPaymentProvider;
    }

    public static Currency getPreferredCurrency() {
        if (localInstance == null) {
            TravelerLog.e("SDK not initialized");
            return null;
        }

        return localInstance.preferredCurrency;
    }

    public static void setPreferredCurrency(Currency currency) {
        if (localInstance == null) {
            TravelerLog.e("SDK not initialized");
            return;
        }

        localInstance.preferredCurrency = currency;
    }
}
