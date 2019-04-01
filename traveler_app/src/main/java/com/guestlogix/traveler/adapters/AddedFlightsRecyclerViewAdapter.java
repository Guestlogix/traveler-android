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


public class AddedFlightsRecyclerViewAdapter extends RecyclerView.Adapter<AddedFlightsRecyclerViewAdapter.ViewHolder> {

    private List<Flight> flightList = new ArrayList<>();
    private View.OnClickListener deleteFlightOnClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_flight_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.item = flightList.get(position);

        holder.departureIata.setText(holder.item.getDepartureAirport().getCode());
        holder.arrivalIata.setText(holder.item.getArrivalAirport().getCode());
        holder.deleteFlight.setOnClickListener(deleteFlightOnClickListener);
        holder.deleteFlight.setTag(position);
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public void update(List<Flight> flights) {
        flightList.clear();
        flightList.addAll(flights);
        notifyDataSetChanged();
    }

    public void setDeleteFlightOnClickListener(View.OnClickListener deleteFlightOnClickListener) {
        this.deleteFlightOnClickListener = deleteFlightOnClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        TextView departureIata;
        TextView arrivalIata;
        Button deleteFlight;
        Flight item;

        ViewHolder(View view) {
            super(view);
            this.view = view;

            departureIata = view.findViewById(R.id.textView_catalog_departureIata);
            arrivalIata = view.findViewById(R.id.textView_catalog_arrivalIata);
            deleteFlight = view.findViewById(R.id.button_catalog_deleteFlights);
        }
    }
}
