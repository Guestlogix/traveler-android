package com.guestlogix.traveler.application;

import android.app.Application;
import com.guestlogix.travelercorekit.Traveler;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class TravelerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Traveler.initialize("testtesttesttesttest", getApplicationContext());
    }

}
