package com.guestlogix.traveleruikit;

import android.content.Context;
import android.content.Intent;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.traveleruikit.activities.CatalogItemDetailsActivity;

public class TravelerUI {
    private static TravelerUI localInstance;

    private PaymentProvider mPaymentProvider;
    private Intent mHomeIntent;

    private TravelerUI(PaymentProvider paymentProvider, Intent homeIntent) {
        mPaymentProvider = paymentProvider;
        mHomeIntent = homeIntent;
    }

    public static void initialize(PaymentProvider paymentProvider, Intent homeIntent) {
        if (localInstance != null) {
            TravelerLog.e("SDK already initialized");
        } else {
            localInstance = new TravelerUI(paymentProvider, homeIntent);
        }
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

    public static Intent getHomeIntent() {
        if (localInstance == null) {
            TravelerLog.e("SDK not initialized");
            return null;
        }

        return localInstance.mHomeIntent;
    }
}
