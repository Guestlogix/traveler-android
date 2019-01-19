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
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.HomeFragmentRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.HomeViewModel;
import com.guestlogix.travelercorekit.Traveler;
import com.guestlogix.travelercorekit.models.CatalogGroup;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.traveleruikit.widgets.CatalogView;

import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView flightResultRecyclerView;
    CatalogView catalogView;
    List<CatalogGroup> mCatalogGroups;

    View mView;

    private HomeViewModel mViewModel;
    private HomeFragmentRecyclerViewAdapter homeFragmentRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        setupView(mView);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        mViewModel.getFlightsObservable().observe(this, flights -> {
            homeFragmentRecyclerViewAdapter.update(flights);
            CatalogQuery catalogQuery = new CatalogQuery(flights);
            mViewModel.updateCatalog(catalogQuery);

        });

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
        catalogView = view.findViewById(R.id.catalogView);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        homeFragmentRecyclerViewAdapter = new HomeFragmentRecyclerViewAdapter();
        homeFragmentRecyclerViewAdapter.setDeleteFlightOnClickListener(deleteFlightOnClickListener);
        flightResultRecyclerView.setAdapter(homeFragmentRecyclerViewAdapter);
    }

    CatalogView.CatalogViewAdapter catalogViewAdapter = new CatalogView.CatalogViewAdapter() {
        @Override
        public void onBindSection(int sectionPosition, TextView titleTextView) {
            titleTextView.setText(mCatalogGroups.get(sectionPosition).getTitle());
        }

        @Override
        public void onBindItem(int sectionPosition, int itemIndex, ImageView thumbNailImageView, TextView titleTextView, TextView subTitleTextView) {
            CatalogItem item = mCatalogGroups.get(sectionPosition).getItems().get(itemIndex);
            titleTextView.setText(item.getTitle());
            subTitleTextView.setText(item.getSubTitle());
            Traveler.loadImage(mCatalogGroups.get(sectionPosition).getItems().get(itemIndex).getImageURL(), thumbNailImageView);
        }

        @Override
        public void onSeeAllClick(int sectionPosition) {
            Log.v("HomeFragment", "Clicked SeeAll for:" + sectionPosition);
        }

        @Override
        public void onItemClick(int sectionPosition, int itemIndex) {
            Log.v("HomeFragment", "Clicked Item " + itemIndex + " for:" + sectionPosition);

            CatalogItem catalogItem = mCatalogGroups.get(sectionPosition).getItems().get(itemIndex);

            HomeFragmentDirections.CatalogItemDetailsAction directions = HomeFragmentDirections.catalogItemDetailsAction(catalogItem);
            Navigation.findNavController(mView).navigate(directions);

        }

        @Override
        public int getSectionsCount() {
            return mCatalogGroups.size();
        }

        @Override
        public int getSectionItemsCount(int sectionIndex) {
            return mCatalogGroups.get(sectionIndex).getItems().size();
        }
    };

    private void catalogUpdateHandler(List<CatalogGroup> catalogGroups) {
        catalogView.setCatalogViewAdapter(catalogViewAdapter);
        mCatalogGroups = catalogGroups;
    }
}
