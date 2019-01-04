package com.guestlogix.traveler.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        demoFlightSearch();
    }

    private void demoFlightSearch() {
        FlightQuery flightQuery = new FlightQuery("AC1", new Date());

        FlightSearchCallback flightSearchCallback = new FlightSearchCallback() {
            @Override
            public void onFlightSearchSuccess(ArrayList<Flight> flights) {
                Log.d("MainActivity", "onFlightSearchSuccess()");
            }

            @Override
            public void onFlightSearchError(Error error) {
                Log.d("MainActivity", "onFlightSearchError()");
            }
        };

        Traveler.flightSearch(flightQuery, flightSearchCallback);
    }

    private void demoCatalog() {
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

}
