package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.HomeFragmentRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.CatalogViewModel;
import com.guestlogix.travelercorekit.models.CatalogGroup;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.traveleruikit.widgets.CatalogView;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private CatalogView catalogView;
    private List<CatalogGroup> catalogGroups;
    private View view;

    private CatalogViewModel catalogViewModel;
    private HomeFragmentRecyclerViewAdapter homeFragmentRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_catalog, container, false);

        RecyclerView flightResultRecyclerView = view.findViewById(R.id.flightResultRecyclerView);
        catalogView = view.findViewById(R.id.catalogView);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        homeFragmentRecyclerViewAdapter = new HomeFragmentRecyclerViewAdapter();
        homeFragmentRecyclerViewAdapter.setDeleteFlightOnClickListener(deleteFlightOnClickListener);
        flightResultRecyclerView.setAdapter(homeFragmentRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        catalogViewModel = ViewModelProviders.of(getActivity()).get(CatalogViewModel.class);
        catalogViewModel.getFlightsObservable().observe(this, this::flightsUpdateHandler);
        catalogViewModel.getGroupsObservable().observe(this, this::catalogUpdateHandler);

        flightsUpdateHandler(catalogViewModel.getFlights());
    }

    private void updateCatalog(List<Flight> flights) {
        CatalogQuery catalogQuery = new CatalogQuery(flights);
        catalogViewModel.updateCatalog(catalogQuery);
    }

    View.OnClickListener deleteFlightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();
            catalogViewModel.deleteFlight(index);
        }
    };

    CatalogView.CatalogViewAdapter catalogViewAdapter = new CatalogView.CatalogViewAdapter() {
        @Override
        public void onBindSection(int sectionPosition, TextView titleTextView) {
            titleTextView.setText(catalogGroups.get(sectionPosition).getTitle());
        }

        @Override
        public void onBindItem(int sectionPosition, int itemIndex, ImageView thumbNailImageView, TextView titleTextView, TextView subTitleTextView) {
            CatalogItem item = catalogGroups.get(sectionPosition).getItems().get(itemIndex);
            titleTextView.setText(item.getTitle());
            subTitleTextView.setText(item.getSubTitle());
        }

        @Override
        public void onSeeAllClick(int sectionPosition) {
            Log.v("CatalogFragment", "Clicked SeeAll for:" + sectionPosition);
        }

        @Override
        public void onItemClick(int sectionPosition, int itemIndex) {
            Log.v("CatalogFragment", "Clicked Item " + itemIndex + " for:" + sectionPosition);

            CatalogItem catalogItem = catalogGroups.get(sectionPosition).getItems().get(itemIndex);

            CatalogFragmentDirections.CatalogItemDetailsAction directions = CatalogFragmentDirections.catalogItemDetailsAction(catalogItem);
            Navigation.findNavController(view).navigate(directions);
        }

        @Override
        public int getSectionsCount() {
            return catalogGroups.size();
        }

        @Override
        public int getSectionItemsCount(int sectionIndex) {
            return catalogGroups.get(sectionIndex).getItems().size();
        }
    };

    private void catalogUpdateHandler(List<CatalogGroup> catalogGroups) {
        catalogView.setCatalogViewAdapter(catalogViewAdapter);
        this.catalogGroups = catalogGroups;
    }

    private void flightsUpdateHandler(List<Flight> flights) {
        updateCatalog(flights);
        homeFragmentRecyclerViewAdapter.update(flights);
    }
}
