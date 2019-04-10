package com.guestlogix.traveleruikit;

import android.content.Context;
import android.content.Intent;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.traveleruikit.activities.CatalogItemDetailsActivity;

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

    public static Intent getCatalogItemDetailsIntent(CatalogItem catalogItem, Context context) {
        if (localInstance == null) {
            TravelerLog.e("SDK not initialized");
            return null;
        }

        if (catalogItem == null) {
            TravelerLog.e("catalogItem must not be null");
            return null;
        }

        Intent i = new Intent(context, CatalogItemDetailsActivity.class);
        i.putExtra(CatalogItemDetailsActivity.ARG_CATALOG_ITEM, catalogItem);
        return i;
    }

    public static PaymentProvider getPaymentProvider() {
        if (localInstance == null) {
            TravelerLog.e("SDK not initialized");
            return null;
        }

        return localInstance.mPaymentProvider;
    }
}
