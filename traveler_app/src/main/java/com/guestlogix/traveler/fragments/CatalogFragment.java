package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.HomeFragmentRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.CatalogViewModel;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.traveleruikit.fragments.BaseFragment;
import com.guestlogix.traveleruikit.fragments.CatalogWidgetFragment;


import java.util.List;

public class CatalogFragment extends BaseFragment {

    private CatalogWidgetFragment catalogWidgetFragment;
    private CatalogViewModel catalogViewModel;
    private HomeFragmentRecyclerViewAdapter homeFragmentRecyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View catalogFragmentView = inflater.inflate(R.layout.fragment_catalog, container, false);

        catalogWidgetFragment = (CatalogWidgetFragment) getChildFragmentManager().findFragmentById(R.id.catalogFragment);
        RecyclerView flightResultRecyclerView = catalogFragmentView.findViewById(R.id.flightResultRecyclerView);
        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(catalogFragmentView.getContext()));
        homeFragmentRecyclerViewAdapter = new HomeFragmentRecyclerViewAdapter();
        homeFragmentRecyclerViewAdapter.setDeleteFlightOnClickListener(deleteFlightOnClickListener);
        flightResultRecyclerView.setAdapter(homeFragmentRecyclerViewAdapter);

        return catalogFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        catalogViewModel = ViewModelProviders.of(getActivityContext()).get(CatalogViewModel.class);
        catalogViewModel.getObservableFlights().observe(getActivityContext(), this::flightsUpdateHandler);
    }

    private void updateCatalog(List<Flight> flights) {
        catalogWidgetFragment.updateCatalog(flights);
    }

    View.OnClickListener deleteFlightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();
            catalogViewModel.deleteFlight(index);
        }
    };

    private void flightsUpdateHandler(List<Flight> flights) {
        updateCatalog(flights);
        homeFragmentRecyclerViewAdapter.update(flights);
    }
}
