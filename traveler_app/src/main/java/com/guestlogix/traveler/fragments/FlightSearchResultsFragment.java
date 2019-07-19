package com.guestlogix.traveler.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.FlightsAdapter;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;
import java.util.List;


public class FlightSearchResultsFragment extends Fragment implements FlightsAdapter.Listener {
    static public String TAG = "FlightSearchResultsFragment";
    static public String ARG_FLIGHTS = "ARG_FLIGHTS";

    public interface InteractionListener {
        void onAddFlight(Flight flight);
        boolean canAddFlight(Flight flight);
    }

    private InteractionListener interactionListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_search_results, container, false);

        if (getArguments() == null || !getArguments().containsKey(ARG_FLIGHTS)) {
            Log.e(TAG, "No List<Flight>");
            return view;
        }


        List<Flight> flights = (ArrayList<Flight>) getArguments().get(ARG_FLIGHTS);
        FlightsAdapter adapter = new FlightsAdapter(flights, this);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_flightResults);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public static FlightSearchResultsFragment newInstance(ArrayList<Flight> flights) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FLIGHTS, flights);
        FlightSearchResultsFragment fragment = new FlightSearchResultsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAddFlight(Flight flight) {
        if (interactionListener != null)
            interactionListener.onAddFlight(flight);
    }

    @Override
    public boolean canAddFlight(Flight flight) {
        return interactionListener.canAddFlight(flight);
    }
}
