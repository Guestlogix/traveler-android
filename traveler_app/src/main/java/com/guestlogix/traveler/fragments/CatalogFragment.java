package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.AddedFlightsRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.CatalogViewModel;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.traveleruikit.fragments.BaseFragment;
import com.guestlogix.traveleruikit.fragments.CatalogWidgetFragment;

import java.util.List;

public class CatalogFragment extends BaseFragment implements CatalogWidgetFragment.CatalogFetchSuccessCallback {

    private CatalogWidgetFragment catalogWidgetFragment;
    private CatalogViewModel catalogViewModel;
    private AddedFlightsRecyclerViewAdapter adapter;
    private boolean canDeleteFlight = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View catalogFragmentView = inflater.inflate(R.layout.fragment_catalog, container, false);

        catalogWidgetFragment = (CatalogWidgetFragment) getChildFragmentManager().findFragmentById(R.id.fragment_catalogFragment_catalogContainer);
        if (catalogWidgetFragment != null) {
            catalogWidgetFragment.setCatalogFetchSuccessCallback(this);
        }

        // Setup flights recycler view.
        RecyclerView flightResultRecyclerView = catalogFragmentView.findViewById(R.id.recyclerView_catalogFragment_addedFlights);
        LinearLayoutManager layoutManager = new LinearLayoutManager(flightResultRecyclerView.getContext());
        adapter = new AddedFlightsRecyclerViewAdapter();
        adapter.setDeleteFlightOnClickListener(this::onDeleteClick);
        DividerItemDecoration decorator = new DividerItemDecoration
                (flightResultRecyclerView.getContext(), layoutManager.getOrientation());

        // Bind recycler view data.
        flightResultRecyclerView.setLayoutManager(layoutManager);
        flightResultRecyclerView.setAdapter(adapter);
        flightResultRecyclerView.addItemDecoration(decorator);

        return catalogFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        catalogViewModel = ViewModelProviders.of(getActivityContext()).get(CatalogViewModel.class);
        catalogViewModel.getObservableFlights().observe(getActivityContext(), this::flightsUpdateHandler);
    }

    @Override
    public void onCatalogFetchSuccess() {
        // Enable deleting flights
        canDeleteFlight = true;
    }

    // TODO: This is not a good solution for a UX stand point. Find a better strategy!
    private void onDeleteClick(View v) {
        if (canDeleteFlight) {
            int index = (Integer) v.getTag();
            catalogViewModel.deleteFlight(index);
        }
    }

    private void flightsUpdateHandler(List<Flight> flights) {
        canDeleteFlight = false;
        catalogWidgetFragment.updateCatalog(flights);
        adapter.update(flights);
    }
}
