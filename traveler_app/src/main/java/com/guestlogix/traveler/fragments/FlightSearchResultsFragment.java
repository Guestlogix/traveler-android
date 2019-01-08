package com.guestlogix.traveler.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.FlightSearchResultRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.FlightSearchResultViewModel;
import com.guestlogix.traveler.viewmodels.HomeViewModel;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FlightSearchResultsFragment extends Fragment {

    @BindView(R.id.flightResultRecyclerView)
    RecyclerView flightResultRecyclerView;

    private View mView;

    private FlightSearchResultViewModel mFlightSearchResultViewModel;
    private HomeViewModel mHomeViewModel;
    private FlightSearchResultRecyclerViewAdapter flightSearchResultRecyclerViewAdapter;

    public FlightSearchResultsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FlightSearchResultsFragmentArgs arg = FlightSearchResultsFragmentArgs.fromBundle(getArguments());

        String departureDate = arg.getDepartureDate();
        String flightNumber = arg.getFlightNumber();

        try {
            Date date = DateHelper.getDateAsObject(departureDate);
            FlightQuery flightQuery = new FlightQuery(flightNumber, date);

            mFlightSearchResultViewModel = ViewModelProviders.of(getActivity()).get(FlightSearchResultViewModel.class);
            mHomeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

            mFlightSearchResultViewModel.getFlightsObservable().observe(this, flights -> {
                flightSearchResultRecyclerViewAdapter.update(flights);
            });

            mFlightSearchResultViewModel.flightSearch(flightQuery);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_flight_search_results, container, false);

        setupView(mView);

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupView(View view) {

        ButterKnife.bind(this, view);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        flightSearchResultRecyclerViewAdapter = new FlightSearchResultRecyclerViewAdapter();
        flightSearchResultRecyclerViewAdapter.setAddFlightOnClickListener(onAddFlight);
        flightResultRecyclerView.setAdapter(flightSearchResultRecyclerViewAdapter);
    }

    View.OnClickListener onAddFlight = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //TODO Add flights directly to HomeFragmentViewHolder then navigate

            int index = (Integer) v.getTag();
            Flight flight = mFlightSearchResultViewModel.getFlightsObservable().getValue().get(index);

            FlightSearchResultsFragmentDirections.HomeAction directions = FlightSearchResultsFragmentDirections.homeAction(flight);

            Navigation.findNavController(mView).navigate(directions);


        }
    };


}
