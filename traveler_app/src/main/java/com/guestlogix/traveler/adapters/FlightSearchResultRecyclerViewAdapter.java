package com.guestlogix.traveler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Flight}
 */
public class FlightSearchResultRecyclerViewAdapter extends RecyclerView.Adapter<FlightSearchResultRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Flight> mFlightsArrayList = new ArrayList<>();
    private View.OnClickListener addFlightOnClickListener;

    public void setAddFlightOnClickListener(View.OnClickListener addFlightOnClickListener) {
        this.addFlightOnClickListener = addFlightOnClickListener;
    }

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
        holder.departureIataTextView.setText(mFlightsArrayList.get(position).getDepartureAirport().getCode());
        holder.departureTimeTextView.setText(DateHelper.getTimeAsString(mFlightsArrayList.get(position).getDepartureDate()));
        holder.arrivalCityTextView.setText(mFlightsArrayList.get(position).getArrivalAirport().getCity());
        holder.arrivalIataTextView.setText(mFlightsArrayList.get(position).getArrivalAirport().getCode());
        holder.arrivalTimeTextView.setText(DateHelper.getTimeAsString(mFlightsArrayList.get(position).getArrivalDate()));

        holder.addFlightTextView.setTag(position);
        holder.addFlightTextView.setOnClickListener(addFlightOnClickListener);

    }

    @Override
    public int getItemCount() {
        return mFlightsArrayList.size();
    }

    public void update(List<Flight> flightsArrayList) {
        mFlightsArrayList.clear();
        mFlightsArrayList.addAll(flightsArrayList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView departureCityTextView;
        TextView departureIataTextView;
        TextView departureTimeTextView;
        TextView arrivalCityTextView;
        TextView arrivalIataTextView;
        TextView arrivalTimeTextView;
        TextView addFlightTextView;

        Flight mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            departureCityTextView = mView.findViewById(R.id.departureCityTextView);
            departureIataTextView = mView.findViewById(R.id.departureIataTextView);
            departureTimeTextView = mView.findViewById(R.id.departureTimeTextView);

            arrivalCityTextView = mView.findViewById(R.id.arrivalCityTextView);
            arrivalIataTextView = mView.findViewById(R.id.arrivalIataTextView);
            arrivalTimeTextView = mView.findViewById(R.id.arrivalTimeTextView);

            addFlightTextView = mView.findViewById(R.id.addFlightTextView);
        }
    }
}
