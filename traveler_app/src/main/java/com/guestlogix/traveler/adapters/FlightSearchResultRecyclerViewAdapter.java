package com.guestlogix.traveler.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guestlogix.traveler.R;
import com.guestlogix.traveler.fragments.FlightSearchResultsFragment.OnListFragmentInteractionListener;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Flight} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FlightSearchResultRecyclerViewAdapter extends RecyclerView.Adapter<FlightSearchResultRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Flight> mFlightsArrayList = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_flight_search_results_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mFlightsArrayList.get(position);
        holder.departureCityTextView.setText(mFlightsArrayList.get(position).getDepartureAirport().getCity());
        holder.arrivalCityTextView.setText(mFlightsArrayList.get(position).getArrivalAirport().getCity());


        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFlightsArrayList.size();
    }

    public void setInteractionListener(OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public void update(ArrayList<Flight> flightsArrayList) {
        mFlightsArrayList.clear();
        mFlightsArrayList.addAll(flightsArrayList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView departureCityTextView;
        public final TextView departureIataTextView;
        public final TextView departureTimeTextView;
        public final TextView arrivalCityTextView;
        public final TextView arrivalIataTextView;
        public final TextView arrivalTimeTextView;

        public Flight mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            departureCityTextView = mView.findViewById(R.id.departureCityTextView);
            departureIataTextView = mView.findViewById(R.id.departureIataTextView);
            departureTimeTextView = mView.findViewById(R.id.departureTimeTextView);

            arrivalCityTextView = mView.findViewById(R.id.arrivalCityTextView);
            arrivalIataTextView = mView.findViewById(R.id.arrivalIataTextView);
            arrivalTimeTextView = mView.findViewById(R.id.arrivalTimeTextView);
        }
    }
}
