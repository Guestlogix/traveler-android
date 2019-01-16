package com.guestlogix.traveler.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.FlightSearchResultRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.FlightSearchResultViewModel;
import com.guestlogix.traveler.viewmodels.HomeViewModel;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.utilities.DateHelper;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class FlightSearchResultsFragment extends Fragment {

    RecyclerView flightResultRecyclerView;
    LinearLayout emptyListLayout;

    private View mView;

    private FlightSearchResultViewModel mFlightSearchResultViewModel;
    private HomeViewModel mHomeViewModel;
    private FlightSearchResultRecyclerViewAdapter flightSearchResultRecyclerViewAdapter;

    public FlightSearchResultsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FlightSearchResultsFragmentArgs arg = FlightSearchResultsFragmentArgs.fromBundle(getArguments());

        String departureDate = arg.getDepartureDate();
        String flightNumber = arg.getFlightNumber();

        try {
            Date date = DateHelper.getDateAsObject(departureDate);
            FlightQuery flightQuery = new FlightQuery(flightNumber, date);

            mFlightSearchResultViewModel = ViewModelProviders.of(this).get(FlightSearchResultViewModel.class);
            mHomeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

            mFlightSearchResultViewModel.flightSearch(flightQuery);
            mFlightSearchResultViewModel.getFlightsObservable().observe(this, this::flightsUpdateHandler);

            // TODO Add a progressbar
            Toast.makeText(getActivity(), "Searching flights...", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Something went wrong, please try again...", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_flight_search_results, container, false);

        setupView(mView);

        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupView(View view) {

        flightResultRecyclerView = view.findViewById(R.id.flightResultRecyclerView);
        emptyListLayout = view.findViewById(R.id.emptyListLayout);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        flightSearchResultRecyclerViewAdapter = new FlightSearchResultRecyclerViewAdapter();
        flightSearchResultRecyclerViewAdapter.setAddFlightOnClickListener(this::onAddFlight);
        flightResultRecyclerView.setAdapter(flightSearchResultRecyclerViewAdapter);

        view.findViewById(R.id.try_again).setOnClickListener(this::tryAgainHandler);
    }

    private void tryAgainHandler(View view) {
        Navigation.findNavController(view).navigate(R.id.flight_search_action);
    }

    private void flightsUpdateHandler(List<Flight> flights) {
        if (flights.isEmpty()) {
            flightResultRecyclerView.setVisibility(View.GONE);
            emptyListLayout.setVisibility(View.VISIBLE);
        } else {
            flightSearchResultRecyclerViewAdapter.update(flights);
            flightResultRecyclerView.setVisibility(View.VISIBLE);
            emptyListLayout.setVisibility(View.GONE);
        }
    }

    private void onAddFlight(View v) {
        int index = (Integer) v.getTag();
        Flight flight = mFlightSearchResultViewModel.getFlightsObservable().getValue().get(index);
        mHomeViewModel.addFlight(flight);

        Navigation.findNavController(mView).navigate(R.id.home_action);
    }
}
