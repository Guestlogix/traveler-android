package com.guestlogix.traveler.application;


import androidx.multidex.MultiDexApplication;

import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler_stripe_payment_provider.StripePaymentProvider;
import com.guestlogix.travelercorekit.models.Currency;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.TravelerUI;

public class TravelerApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Traveler.initialize(BuildConfig.TRAVELER_API_KEY, getApplicationContext(), true);
        TravelerUI.initialize(new StripePaymentProvider(), Currency.USD);
    }
}
