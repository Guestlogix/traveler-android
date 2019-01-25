package com.guestlogix.traveler.fragments;

import android.app.AlertDialog;
import android.content.Context;
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
import com.guestlogix.traveler.viewmodels.FlightSearchResultViewModel;
import com.guestlogix.traveler.viewmodels.CatalogViewModel;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.utilities.DateHelper;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class FlightSearchResultsFragment extends Fragment {

    private View view;
    private RecyclerView flightResultRecyclerView;
    private LinearLayout emptyListLayout;
    private FlightSearchResultViewModel flightSearchResultViewModel;
    private CatalogViewModel catalogViewModel;
    private FlightSearchResultRecyclerViewAdapter flightSearchResultRecyclerViewAdapter;
    private ProgressBar flightResultProgressbar;

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

            flightSearchResultViewModel = ViewModelProviders.of(this).get(FlightSearchResultViewModel.class);
            catalogViewModel = ViewModelProviders.of(getActivity()).get(CatalogViewModel.class);

            flightSearchResultViewModel.flightSearch(flightQuery);
            flightSearchResultViewModel.getFlightsObservable().observe(this, this::flightsUpdateHandler);
            flightSearchResultViewModel.getFlightSearchState().observe(this, this::flightStateChangeHandler);

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        Flight flight = flightSearchResultViewModel.getFlightsObservable().getValue().get(index);
        catalogViewModel.addFlight(flight);

        Navigation.findNavController(view).navigate(R.id.home_action);
    }

    private void flightStateChangeHandler(FlightSearchResultViewModel.FlightSearchState state) {
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
                .setNeutralButton(getString(R.string.ok), ((dialog1, which) -> Navigation.findNavController(view).navigate(R.id.home_action)))
                .setCancelable(false)
                .create();

        dialog.show();
    }
}
