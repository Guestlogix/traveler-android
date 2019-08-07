package com.guestlogix.traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.fragments.FlightSearchFragment;
import com.guestlogix.traveler.fragments.FlightSearchResultsFragment;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchActivity extends AppCompatActivity
        implements
        FlightSearchFragment.InteractionListener,
        FlightSearchCallback,
        RetryFragment.InteractionListener,
        FlightSearchResultsFragment.InteractionListener {

    static private String ARG_QUERY = "ARG_QUERY";
    static private String ARG_EXCLUDED_FLIGHTS = "ARG_EXCLUDED_FLIGHTS";
    static public String EXTRA_FLIGHT = "EXTRA_FLIGHT";

    private FlightQuery query;
    private List<Flight> flightsToExclude;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_QUERY)) {
            query = (FlightQuery) savedInstanceState.get(ARG_QUERY);
        }

        if (getIntent().hasExtra(ARG_EXCLUDED_FLIGHTS)) {
            flightsToExclude = (List<Flight>) getIntent().getSerializableExtra(ARG_EXCLUDED_FLIGHTS);
        } else {
            flightsToExclude = new ArrayList<>();
        }

        transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        setContentView(R.layout.activity_flight_search);

        Fragment searchFragment = new FlightSearchFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.layout_flightSearch_container, searchFragment);
        transactionQueue.addTransaction(transaction);
    }

    // FlightSearchFragment.InteractionListener

    @Override
    public void onFlightSearch(FlightQuery query) {
        this.query = query;

        Fragment fragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.layout_flightSearch_container, fragment);
        transactionQueue.addTransaction(transaction);

        Traveler.flightSearch(query, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        if (query != null)
            outState.putSerializable(ARG_QUERY, query);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof RetryFragment) {
            ((RetryFragment) fragment).setInteractionListener(this);
        } else if (fragment instanceof FlightSearchFragment) {
            ((FlightSearchFragment) fragment).setInteractionListener(this);
        } else if (fragment instanceof FlightSearchResultsFragment) {
            ((FlightSearchResultsFragment) fragment).setInteractionListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        transactionQueue.setSuspended(true);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        transactionQueue.setSuspended(false);
    }

    // FlightSearchCallback

    @Override
    public void onFlightSearchSuccess(List<Flight> flights) {
        ArrayList<Flight> flightsArray = new ArrayList<>(flights);
        Fragment fragment = FlightSearchResultsFragment.newInstance(flightsArray);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.layout_flightSearch_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onFlightSearchError(Error error) {
        Fragment fragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.layout_flightSearch_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    // FlightSearchCallback, RetryFragment.InteractionListener

    @Override
    public void onRetry() {
        onFlightSearch(query);
    }

    // FlightSearchResultsFragment.InteractionListener

    @Override
    public void onAddFlight(Flight flight) {
        Intent data = new Intent();
        data.putExtra(EXTRA_FLIGHT, flight);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean canAddFlight(Flight flight) {
        for (Flight f : flightsToExclude) {
            if (f.equals(flight)) {
                return false;
            }
        }

        return true;
    }
}
