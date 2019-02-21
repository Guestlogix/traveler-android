package com.guestlogix.traveler.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
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
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.utilities.DateHelper;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.guestlogix.traveler.viewmodels.CatalogViewModel.EXRTA_FLIGHT;

/**
 * A fragment representing a list of Flights.
 */
public class SearchFlightResultsFragment extends Fragment {

    private View view;
    private RecyclerView flightResultRecyclerView;
    private LinearLayout emptyListLayout;
    private SearchFlightResultViewModel searchFlightResultViewModel;
    private FlightSearchResultRecyclerViewAdapter flightSearchResultRecyclerViewAdapter;
    private ProgressBar flightResultProgressbar;

    public SearchFlightResultsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SearchFlightResultsFragmentArgs arg = SearchFlightResultsFragmentArgs.fromBundle(getArguments());

        String departureDate = arg.getDepartureDate();
        String flightNumber = arg.getFlightNumber();

        try {
            Date date = DateHelper.getDateTimeAsObject(departureDate);
            FlightQuery flightQuery = new FlightQuery(flightNumber, date);

            searchFlightResultViewModel = ViewModelProviders.of(this).get(SearchFlightResultViewModel.class);

            searchFlightResultViewModel.flightSearch(flightQuery);
            searchFlightResultViewModel.getObservableFlights().observe(this, this::flightsUpdateHandler);
            searchFlightResultViewModel.getFlightSearchState().observe(this, this::flightStateChangeHandler);

        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Something went wrong, please try again...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flight_search_results, container, false);

        flightResultRecyclerView = view.findViewById(R.id.flightResultRecyclerView);
        emptyListLayout = view.findViewById(R.id.emptyListLayout);
        flightResultProgressbar = view.findViewById(R.id.flightResultProgressBar);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        flightSearchResultRecyclerViewAdapter = new FlightSearchResultRecyclerViewAdapter();
        flightSearchResultRecyclerViewAdapter.setAddFlightOnClickListener(this::onAddFlight);
        flightResultRecyclerView.setAdapter(flightSearchResultRecyclerViewAdapter);

        view.findViewById(R.id.try_again).setOnClickListener(this::tryAgainHandler);
        return view;
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
        Flight flight = searchFlightResultViewModel.getObservableFlights().getValue().get(index);
        Intent data = new Intent();
        data.putExtra(EXRTA_FLIGHT, flight);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }

    private void flightStateChangeHandler(SearchFlightResultViewModel.FlightSearchState state) {
        switch (state) {
            case LOADING:
                emptyListLayout.setVisibility(View.GONE);
                flightResultRecyclerView.setVisibility(View.GONE);
                flightResultProgressbar.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                flightResultProgressbar.setVisibility(View.GONE);
                break;
            case ERROR:
                onFlightResultError();
                break;
        }
    }

    private void onFlightResultError() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.oh_no))
                .setMessage(getString(R.string.something_went_wrong))
                .setNeutralButton(getString(R.string.ok), ((dialog1, which) -> getActivity().finish()))
                .setCancelable(false)
                .create();

        dialog.show();
    }
}
