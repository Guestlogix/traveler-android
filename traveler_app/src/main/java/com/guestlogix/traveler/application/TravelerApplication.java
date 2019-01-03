package com.guestlogix.traveler.application;

import android.app.Application;
import com.guestlogix.travelercorekit.Traveler;

public class TravelerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Traveler.initialize("testtesttesttesttest", getApplicationContext());
    }

}
