package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.AddedFlightsRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.HomeViewModel;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.traveleruikit.fragments.BaseFragment;
import com.guestlogix.traveleruikit.fragments.CatalogFragment;
import com.guestlogix.traveleruikit.fragments.TravelerFragmentRetryFragment;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.List;

public class HomeContainerFragment extends BaseFragment {

    private HomeViewModel catalogViewModel;
    private AddedFlightsRecyclerViewAdapter flightsRecyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View homeContainerFragmentView = inflater.inflate(R.layout.fragment_home_container, container, false);

        // Setup flights recycler view.
        RecyclerView flightResultRecyclerView = homeContainerFragmentView.findViewById(R.id.recyclerView_catalogFragment_addedFlights);
        LinearLayoutManager layoutManager = new LinearLayoutManager(flightResultRecyclerView.getContext());
        flightsRecyclerViewAdapter = new AddedFlightsRecyclerViewAdapter();
        flightsRecyclerViewAdapter.setDeleteFlightOnClickListener(deleteFlightOnClickListener);
        flightsRecyclerViewAdapter.setViewFlightClickListener(this::onViewFlightClick);
        DividerItemDecoration decorator = new DividerItemDecoration
                (flightResultRecyclerView.getContext(), layoutManager.getOrientation());

        // Bind recycler view data.
        flightResultRecyclerView.setLayoutManager(layoutManager);
        flightResultRecyclerView.setAdapter(flightsRecyclerViewAdapter);
        flightResultRecyclerView.addItemDecoration(decorator);

        return homeContainerFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        catalogViewModel = ViewModelProviders.of(getActivityContext()).get(HomeViewModel.class);
        catalogViewModel.getObservableFlights().observe(getActivityContext(), this::flightsUpdateHandler);
        catalogViewModel.getStatus().observe(getActivityContext(), this::onStateChange);

        getActivity().setTitle(R.string.app_name);
    }

    private void onViewFlightClick(View v) {
        int index = (Integer) v.getTag();

        Flight flight = catalogViewModel.getObservableFlights().getValue().get(index);
        HomeContainerFragmentDirections.FlightInformationAction action = HomeContainerFragmentDirections.flightInformationAction(flight);
        Navigation.findNavController(getActivityContext(), R.id.homeHostFragment).navigate(action);
    }

    private void onStateChange(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                LoadingFragment loadingFragment = new LoadingFragment();

                FragmentTransaction transaction = getActivityContext().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.catalogContainer, loadingFragment);

                transaction.commit();
                break;
            case SUCCESS:
                CatalogFragment catalogFragment = CatalogFragment.newInstance(catalogViewModel.getCatalog());

                transaction = getActivityContext().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.catalogContainer, catalogFragment);

                transaction.commit();
                break;
            case ERROR:
                TravelerFragmentRetryFragment errorFragment = TravelerFragmentRetryFragment.getInstance(getString(com.guestlogix.traveleruikit.R.string.label_sorry),
                        getString(com.guestlogix.traveleruikit.R.string.label_nothing_to_show),
                        getString(com.guestlogix.traveleruikit.R.string.try_again));

                errorFragment.setOnInteractionListener(() -> catalogViewModel.fetchCatalog());

                transaction = getActivityContext().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.catalogContainer, errorFragment);

                transaction.commit();
                break;
        }
    }

    private View.OnClickListener deleteFlightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();
            catalogViewModel.deleteFlight(index);
        }
    };

    private void flightsUpdateHandler(List<Flight> flights) {
        flightsRecyclerViewAdapter.update(flights);
    }
}
