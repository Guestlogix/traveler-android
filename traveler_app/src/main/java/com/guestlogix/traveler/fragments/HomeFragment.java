package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.HomeFragmentRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.HomeViewModel;
import com.guestlogix.travelercorekit.models.Group;

import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView flightResultRecyclerView;

    private HomeViewModel mViewModel;
    private HomeFragmentRecyclerViewAdapter homeFragmentRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        mViewModel.getFlightsObservable().observe(this, flights -> homeFragmentRecyclerViewAdapter.update(flights));
        mViewModel.getGroupsObservable().observe(this, this::catalogUpdateHandler);
    }

    View.OnClickListener deleteFlightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();
            mViewModel.deleteFlight(index);
        }
    };

    private void setupView(View view) {

        flightResultRecyclerView = view.findViewById(R.id.flightResultRecyclerView);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        homeFragmentRecyclerViewAdapter = new HomeFragmentRecyclerViewAdapter();
        homeFragmentRecyclerViewAdapter.setDeleteFlightOnClickListener(deleteFlightOnClickListener);
        flightResultRecyclerView.setAdapter(homeFragmentRecyclerViewAdapter);
    }

    private void catalogUpdateHandler(List<Group> groups) {
        // TODO: Implement this
    }
}
