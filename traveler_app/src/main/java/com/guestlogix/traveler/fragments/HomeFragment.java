package com.guestlogix.traveler.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.FlightSearchResultRecyclerViewAdapter;
import com.guestlogix.traveler.adapters.HomeFragmentRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.HomeViewModel;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    @BindView(R.id.flightResultRecyclerView)
    RecyclerView flightResultRecyclerView;

    private HomeViewModel mViewModel;
    private HomeFragmentRecyclerViewAdapter homeFragmentRecyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        mViewModel.getFlightsObservable().observe(this, new Observer<ArrayList<Flight>>() {
            @Override
            public void onChanged(ArrayList<Flight> flights) {
                homeFragmentRecyclerViewAdapter.update(flights);
            }
        });

        Bundle bundle = getArguments();
        if (null != bundle) {
            HomeFragmentArgs arg = HomeFragmentArgs.fromBundle(bundle);
            Flight flight = arg.getFlight();

            mViewModel.addFlight(flight);
        }
    }

    private void setupView(View view) {

        ButterKnife.bind(this, view);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        homeFragmentRecyclerViewAdapter = new HomeFragmentRecyclerViewAdapter();
        flightResultRecyclerView.setAdapter(homeFragmentRecyclerViewAdapter);
    }

}
