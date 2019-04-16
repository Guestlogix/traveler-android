package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.widgets.FlightCardsRecyclerView;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment which displays the information for a list of flights.
 */
public class FlightInformationFragment extends Fragment {

    private Flight flight;

    public FlightInformationFragment() {
        // Do nothing.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FlightInformationFragmentArgs args = FlightInformationFragmentArgs.fromBundle(getArguments());
        flight = args.getFlight();
        getActivity().setTitle(String.format("%s - %s", flight.getNumber(), DateHelper.formatDate(flight.getDepartureDate())));
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flight_information, container, false);

        FlightCardsRecyclerView recyclerView = v.findViewById(R.id.flightCards_flightInformation_container);

        List<Flight> flights = new ArrayList<>();
        flights.add(flight);
        recyclerView.setFlights(flights);

        return v;
    }
}
