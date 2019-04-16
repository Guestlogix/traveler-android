package com.guestlogix.traveler.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.FlightSearchResultRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.SearchFlightResultViewModel;
import com.guestlogix.traveler.widgets.FlightCardsRecyclerView;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.traveleruikit.fragments.BaseFragment;


import java.util.List;

import static com.guestlogix.traveler.viewmodels.HomeViewModel.EXTRA_FLIGHT;

/**
 * A fragment representing a list of Flights.
 */
public class SearchFlightResultsFragment extends BaseFragment {

    private SearchFlightResultViewModel searchFlightResultViewModel;

    public SearchFlightResultsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchFlightResultViewModel = ViewModelProviders.of(getActivityContext()).get(SearchFlightResultViewModel.class);

        View view = inflater.inflate(R.layout.fragment_flight_search_results, container, false);

        FlightCardsRecyclerView recyclerView = view.findViewById(R.id.flightCards_searchFlightResult_addFlights);
        searchFlightResultViewModel.getObservableFlights().observe(this, recyclerView::setFlights);
        recyclerView.setOnAddFlightListener(this::onAddFlight);

        return view;
    }

//    private void tryAgainHandler(View view) {
//        Navigation.findNavController(view).navigate(R.id.flight_search_action);
//    }

    private void onAddFlight(int index) {
        Flight flight = searchFlightResultViewModel.getObservableFlights().getValue().get(index);
        Intent data = new Intent();
        data.putExtra(EXTRA_FLIGHT, flight);
        getActivityContext().setResult(Activity.RESULT_OK, data);
        getActivityContext().finish();
    }

}
