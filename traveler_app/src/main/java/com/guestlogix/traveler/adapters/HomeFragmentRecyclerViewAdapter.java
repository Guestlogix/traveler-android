package com.guestlogix.traveler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;
import java.util.List;


public class HomeFragmentRecyclerViewAdapter extends RecyclerView.Adapter<HomeFragmentRecyclerViewAdapter.ViewHolder> {

    private List<Flight> flightList = new ArrayList<>();
    private View.OnClickListener deleteFlightOnClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_flight_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = flightList.get(position);

        holder.departureIataTextView.setText(flightList.get(position).getDepartureAirport().getCode());
        holder.arrivalIataTextView.setText(flightList.get(position).getArrivalAirport().getCode());
        holder.deleteTextView.setOnClickListener(deleteFlightOnClickListener);
        holder.deleteTextView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public void update(List<Flight> flightsArrayList) {
        flightList.clear();
        flightList.addAll(flightsArrayList);
        notifyDataSetChanged();
    }

    public void setDeleteFlightOnClickListener(View.OnClickListener deleteFlightOnClickListener) {
        this.deleteFlightOnClickListener = deleteFlightOnClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        TextView departureIataTextView;
        TextView arrivalIataTextView;
        TextView deleteTextView;
        Flight item;

        ViewHolder(View view) {
            super(view);
            this.view = view;

            departureIataTextView = view.findViewById(R.id.departureIataTextView);
            arrivalIataTextView = view.findViewById(R.id.arrivalIataTextView);
            deleteTextView = view.findViewById(R.id.deleteTextView);
        }
    }
}
