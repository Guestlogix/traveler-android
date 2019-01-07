package com.guestlogix.traveler.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.FlightSearchResultRecyclerViewAdapter;
import com.guestlogix.traveler.viewmodels.FlightSearchResultViewModel;
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
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FlightSearchResultsFragment extends Fragment {

    @BindView(R.id.flightResultRecyclerView)
    RecyclerView flightResultRecyclerView;

    private OnListFragmentInteractionListener mListener;
    private FlightSearchResultViewModel mFlightSearchResultViewModel;
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

            mFlightSearchResultViewModel = ViewModelProviders.of(this).get(FlightSearchResultViewModel.class);
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
        View view = inflater.inflate(R.layout.fragment_flight_search_results, container, false);

        setupView(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setupView(View view) {

        ButterKnife.bind(this, view);

        flightResultRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        flightSearchResultRecyclerViewAdapter = new FlightSearchResultRecyclerViewAdapter();
        flightSearchResultRecyclerViewAdapter.setInteractionListener(mListener);
        flightResultRecyclerView.setAdapter(flightSearchResultRecyclerViewAdapter);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Flight item);
    }
}
