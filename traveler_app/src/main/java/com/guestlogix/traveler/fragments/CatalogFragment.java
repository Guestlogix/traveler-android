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
import com.guestlogix.traveler.viewmodels.CatalogViewModel;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.traveleruikit.fragments.CatalogWidgetFragment;

import java.util.List;

public class CatalogFragment extends Fragment {

    private View catalogFragmentView;
    private CatalogWidgetFragment catalogWidgetFragment;
    private CatalogViewModel catalogViewModel;
    private HomeFragmentRecyclerViewAdapter homeFragmentRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        catalogFragmentView = inflater.inflate(R.layout.fragment_catalog, container, false);
        //TODO Find a better way to set fragment attributes
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
        catalogViewModel = ViewModelProviders.of(getActivity()).get(CatalogViewModel.class);
        catalogViewModel.getFlightsObservable().observe(this, this::flightsUpdateHandler);

        flightsUpdateHandler(catalogViewModel.getFlights());
    }

    private void updateCatalog(List<Flight> flights) {
        CatalogQuery catalogQuery = new CatalogQuery(flights);
        catalogViewModel.updateCatalog(catalogQuery);
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
