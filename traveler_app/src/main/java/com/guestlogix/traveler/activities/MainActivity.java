package com.guestlogix.traveler.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.fragments.FlightSearchResultsFragment;
import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements FlightSearchResultsFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //demoFlightSearch();
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

    @Override
    public void onListFragmentInteraction(Flight item) {
        //Navigation.(R.id.next_action, null)
        NavController navController = Navigation.findNavController(findViewById(R.id.my_nav_host_fragment));
        navController.navigate(R.id.home_action);
    }
}
