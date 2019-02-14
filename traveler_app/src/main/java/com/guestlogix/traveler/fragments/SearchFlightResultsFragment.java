package com.guestlogix.traveler.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.FlightSearchResultRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.SearchFlightResultViewModel;
import com.guestlogix.travelercorekit.models.Flight;


import java.util.List;

import static com.guestlogix.traveler.viewmodels.CatalogViewModel.EXTRA_FLIGHT;

/**
 * A fragment representing a list of Flights.
 */
public class SearchFlightResultsFragment extends Fragment {

    private View view;
    private RecyclerView flightResultRecyclerView;
    private SearchFlightResultViewModel searchFlightResultViewModel;
    private FlightSearchResultRecyclerViewAdapter flightSearchResultRecyclerViewAdapter;

    public SearchFlightResultsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchFlightResultViewModel = ViewModelProviders.of(getActivity()).get(SearchFlightResultViewModel.class);
        searchFlightResultViewModel.getObservableFlights().observe(this, this::flightsUpdateHandler);

        view = inflater.inflate(R.layout.fragment_flight_search_results, container, false);

        flightResultRecyclerView = view.findViewById(R.id.flightResultRecyclerView);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        flightSearchResultRecyclerViewAdapter = new FlightSearchResultRecyclerViewAdapter();
        flightSearchResultRecyclerViewAdapter.setAddFlightOnClickListener(this::onAddFlight);
        flightResultRecyclerView.setAdapter(flightSearchResultRecyclerViewAdapter);

        return view;
    }

    private void tryAgainHandler(View view) {
        Navigation.findNavController(view).navigate(R.id.flight_search_action);
    }

    private void flightsUpdateHandler(List<Flight> flights) {
        if (null != flights) {
            flightSearchResultRecyclerViewAdapter.update(flights);
        }
    }

    private void onAddFlight(View v) {
        int index = (Integer) v.getTag();
        Flight flight = searchFlightResultViewModel.getObservableFlights().getValue().get(index);
        Intent data = new Intent();
        data.putExtra(EXTRA_FLIGHT, flight);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

}