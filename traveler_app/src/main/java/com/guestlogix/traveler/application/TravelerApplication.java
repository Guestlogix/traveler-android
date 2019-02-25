package com.guestlogix.traveler.application;

import android.app.Application;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler_stripe_payment_provider.StripePaymentProvider;
import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.traveleruikit.TravelerUI;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class TravelerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Traveler.initialize(BuildConfig.TRAVELER_API_KEY, getApplicationContext());
        TravelerUI.initialize(new StripePaymentProvider());
        AppCenter.start(this, BuildConfig.AppCenterKey, Analytics.class, Crashes.class);
    }
}
