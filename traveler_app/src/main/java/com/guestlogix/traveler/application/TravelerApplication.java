package com.guestlogix.traveler.application;

import android.app.Application;

import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler_stripe_payment_provider.StripePaymentManager;
import com.guestlogix.traveler_stripe_payment_provider.StripePaymentProvider;
import com.guestlogix.travelercorekit.models.Currency;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.TravelerUI;

public class TravelerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Traveler.initialize(BuildConfig.TRAVELER_API_KEY, getApplicationContext());
        TravelerUI.initialize(new StripePaymentProvider(), new StripePaymentManager(getApplicationContext()), Currency.USD);
    }
}
