package com.guestlogix.traveler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_flight_search_results_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = flightsList.get(position);
        holder.departureCityTextView.setText(holder.mItem.getDepartureAirport().getCity());
        holder.departureIataTextView.setText(holder.mItem.getDepartureAirport().getCode());
        holder.departureTimeTextView.setText(DateHelper.formatTime(holder.mItem.getDepartureDate()));
        holder.arrivalCityTextView.setText(holder.mItem.getArrivalAirport().getCity());
        holder.arrivalIataTextView.setText(holder.mItem.getArrivalAirport().getCode());
        holder.arrivalTimeTextView.setText(DateHelper.formatTime(holder.mItem.getArrivalDate()));

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
            departureCityTextView = itemView.findViewById(R.id.textView_flightCard_departureCity);
            departureIataTextView = itemView.findViewById(R.id.textView_flightCard_departureIata);
            departureTimeTextView = itemView.findViewById(R.id.textView_flightCard_departureTime);
            arrivalCityTextView = itemView.findViewById(R.id.textView_flightCard_arrivalCity);
            arrivalIataTextView = itemView.findViewById(R.id.textView_catalog_arrivalIata);
            arrivalTimeTextView = itemView.findViewById(R.id.textView_flightCard_arrivalTime);
            addFlightTextView = itemView.findViewById(R.id.textView_flightCard_addFlight);
        }
    }
}
