package com.guestlogix.traveler.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAppCenter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(findViewById(R.id.my_nav_host_fragment))) || super.onOptionsItemSelected(item);
    }

    private void setupAppCenter() {
        AppCenter.start(getApplication(), BuildConfig.AppCenterKey,
                Analytics.class, Crashes.class);
        AppCenter.start(getApplication(), BuildConfig.AppCenterKey, Analytics.class, Crashes.class);
    }
}