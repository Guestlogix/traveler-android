package com.guestlogix.traveler.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guestlogix.traveler.R;
import com.guestlogix.traveler.fragments.FlightSearchResultsFragment.OnListFragmentInteractionListener;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Flight} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FlightSearchResultRecyclerViewAdapter extends RecyclerView.Adapter<FlightSearchResultRecyclerViewAdapter.ViewHolder> {

    private final List<Flight> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FlightSearchResultRecyclerViewAdapter(List<Flight> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_flight_search_results_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.departureCityTextView.setText(mValues.get(position).getDepartureAirport().getCity());
        holder.arrivalCityTextView.setText(mValues.get(position).getArrivalAirport().getCity());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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
