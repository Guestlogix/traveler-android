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

import java.util.ArrayList;
import java.util.List;


public class FlightsSummaryAdapter extends RecyclerView.Adapter<FlightsSummaryAdapter.ViewHolder> {
    public interface Listener {
        void onRemoveFlight(int position);
        void onClick(int position);
    }

    private List<Flight> flightList = new ArrayList<>();
    private Listener listener;

    public FlightsSummaryAdapter(List<Flight> flights, Listener listener) {
        this.listener = listener;
        this.flightList = flights;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_flight_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Flight item = flightList.get(position);

        holder.departureIata.setText(item.getDepartureAirport().getCode());
        holder.arrivalIata.setText(item.getArrivalAirport().getCode());
        holder.deleteFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveFlight(position);
            }
        });
        holder.deleteFlight.setTag(position);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView departureIata;
        TextView arrivalIata;
        Button deleteFlight;

        ViewHolder(View view) {
            super(view);

            departureIata = itemView.findViewById(R.id.textView_flightCard_departureIata);
            arrivalIata = itemView.findViewById(R.id.textView_catalog_arrivalIata);
            deleteFlight = itemView.findViewById(R.id.button_catalog_deleteFlights);
        }
    }
}
