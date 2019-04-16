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
 * An adapter to display flight information cards.
 */
public class FlightCardsRecyclerViewAdapter extends RecyclerView.Adapter<FlightCardsRecyclerViewAdapter.ViewHolder> {

    private List<Flight> flightsList = new ArrayList<>();
    private boolean isFlightAddingEnabled = false;
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
        Flight f = flightsList.get(position);

        holder.departureCityTextView.setText(f.getDepartureAirport().getCity());
        holder.departureIataTextView.setText(f.getDepartureAirport().getCode());
        holder.departureTimeTextView.setText(DateHelper.formatTime(f.getDepartureDate()));
        holder.arrivalCityTextView.setText(f.getArrivalAirport().getCity());
        holder.arrivalIataTextView.setText(f.getArrivalAirport().getCode());
        holder.arrivalTimeTextView.setText(DateHelper.formatTime(f.getArrivalDate()));

        if (isFlightAddingEnabled) {
            holder.addFlightTextView.setAlpha(1f);
            holder.addFlightTextView.setEnabled(true);
            holder.addFlightTextView.setTag(position);
            holder.addFlightTextView.setOnClickListener(addFlightOnClickListener);
        } else {
            holder.addFlightTextView.setEnabled(false);
            holder.addFlightTextView.setAlpha(0.2f);
        }
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

    public void setFlightAddingEnabled(boolean isFlightAddingEnabled) {
        this.isFlightAddingEnabled = isFlightAddingEnabled;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView departureCityTextView;
        TextView departureIataTextView;
        TextView departureTimeTextView;
        TextView arrivalCityTextView;
        TextView arrivalIataTextView;
        TextView arrivalTimeTextView;
        TextView addFlightTextView;

        ViewHolder(View view) {
            super(view);
            departureCityTextView = itemView.findViewById(R.id.textView_flightCard_departureCity);
            departureIataTextView = itemView.findViewById(R.id.textView_flightCard_departureIata);
            departureTimeTextView = itemView.findViewById(R.id.textView_flightCard_departureTime);
            arrivalCityTextView = itemView.findViewById(R.id.textView_flightCard_arrivalCity);
            arrivalIataTextView = itemView.findViewById(R.id.textView_catalog_arrivalIata);
            arrivalTimeTextView = itemView.findViewById(R.id.textView_flightCard_arrivalTime);
            addFlightTextView = itemView.findViewById(R.id.button_flightCard_addFlight);
        }
    }
}
