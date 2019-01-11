package com.guestlogix.traveler.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAppCenter();

//        Traveler.getCatalog("1", new Traveler.CatalogResponseHandler() {
//            @Override
//            public void onSuccess(Catalog catalog) {
//                Log.d("MainActivity", "onSuccess()");
//            }
//
//            @Override
//            public void onError(Error e) {
//                Log.d("MainActivity", "onError()");
//            }
//        });
    }

    private void setupAppCenter() {
        AppCenter.start(getApplication(), BuildConfig.AppCenterKey,
                Analytics.class, Crashes.class);
        AppCenter.start(getApplication(), BuildConfig.AppCenterKey, Analytics.class, Crashes.class);

    }
}
