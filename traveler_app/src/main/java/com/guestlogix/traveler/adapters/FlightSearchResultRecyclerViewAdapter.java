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

    private List<Flight> flightsList = new ArrayList<>();
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
        holder.mItem = flightsList.get(position);
        holder.departureCityTextView.setText(flightsList.get(position).getDepartureAirport().getCity());
        holder.departureIataTextView.setText(flightsList.get(position).getDepartureAirport().getCode());
        holder.departureTimeTextView.setText(DateHelper.formatTime(flightsList.get(position).getDepartureDate()));
        holder.arrivalCityTextView.setText(flightsList.get(position).getArrivalAirport().getCity());
        holder.arrivalIataTextView.setText(flightsList.get(position).getArrivalAirport().getCode());
        holder.arrivalTimeTextView.setText(DateHelper.formatTime(flightsList.get(position).getArrivalDate()));

        holder.addFlightTextView.setTag(position);
        holder.addFlightTextView.setOnClickListener(addFlightOnClickListener);
    }

    @Override
    public int getItemCount() {
        return flightsList.size();
    }

    public void update(List<Flight> flightsArrayList) {
        this.flightsList.clear();
        this.flightsList.addAll(flightsArrayList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
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
            this.view = view;
            departureCityTextView = this.view.findViewById(R.id.departureCityTextView);
            departureIataTextView = this.view.findViewById(R.id.textView_catalog_departureIata);
            departureTimeTextView = this.view.findViewById(R.id.departureTimeTextView);
            arrivalCityTextView = this.view.findViewById(R.id.arrivalCityTextView);
            arrivalIataTextView = this.view.findViewById(R.id.textView_catalog_arrivalIata);
            arrivalTimeTextView = this.view.findViewById(R.id.arrivalTimeTextView);
            addFlightTextView = this.view.findViewById(R.id.addFlightTextView);
        }
    }
}
