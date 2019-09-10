package com.guestlogix.traveler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.utilities.DateHelper;

import java.util.ArrayList;
import java.util.List;


public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.ViewHolder> {
    public interface Listener {
        void onAddFlight(Flight flight);
        boolean canAddFlight(Flight flight);
    }

    // TODO: Make sure these adhere to proper naming convention
    private List<Flight> flightsList;
    private Listener listener;

    public FlightsAdapter(List<Flight> flights, Listener listener) {
        this.listener = listener;
        this.flightsList = flights;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flight_info_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Flight f = flightsList.get(position);

        holder.departureCityTextView.setText(f.getDepartureAirport().getCity());
        holder.departureIataTextView.setText(f.getDepartureAirport().getCode());
        holder.departureTimeTextView.setText(f.getDepartureDateDescription(DateHelper.getTimeFormat()));
        holder.arrivalCityTextView.setText(f.getArrivalAirport().getCity());
        holder.arrivalIataTextView.setText(f.getArrivalAirport().getCode());
        holder.arrivalTimeTextView.setText(f.getArrivalDateDescription(DateHelper.getTimeFormat()));

        boolean isFlightAddingEnabled = listener.canAddFlight(f);

        if (isFlightAddingEnabled) {
            holder.addFlightButton.setAlpha(1f);
            holder.addFlightButton.setEnabled(true);
            holder.addFlightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddFlight(f);
                }
            });
        } else {
            holder.addFlightButton.setEnabled(false);
            holder.addFlightButton.setAlpha(0.2f);
            holder.addFlightButton.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return flightsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView departureCityTextView;
        TextView departureIataTextView;
        TextView departureTimeTextView;
        TextView arrivalCityTextView;
        TextView arrivalIataTextView;
        TextView arrivalTimeTextView;
        Button addFlightButton;

        ViewHolder(View view) {
            super(view);
            departureCityTextView = itemView.findViewById(R.id.textView_flightCard_departureCity);
            departureIataTextView = itemView.findViewById(R.id.textView_flightCard_departureIata);
            departureTimeTextView = itemView.findViewById(R.id.textView_flightCard_departureTime);
            arrivalCityTextView = itemView.findViewById(R.id.textView_flightCard_arrivalCity);
            arrivalIataTextView = itemView.findViewById(R.id.textView_flightCard_arrivalIata);
            arrivalTimeTextView = itemView.findViewById(R.id.textView_flightCard_arrivalTime);
            addFlightButton = itemView.findViewById(R.id.button_flightCard_addFlight);
        }
    }
}
