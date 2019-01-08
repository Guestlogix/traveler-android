package com.guestlogix.traveler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.models.Flight;

import java.util.ArrayList;


public class HomeFragmentRecyclerViewAdapter extends RecyclerView.Adapter<HomeFragmentRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Flight> mFlightsArrayList = new ArrayList<>();
    private View.OnClickListener deleteFlightOnClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_flight_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mFlightsArrayList.get(position);
        holder.departureIataTextView.setText(mFlightsArrayList.get(position).getDepartureAirport().getCode());
        holder.arrivalIataTextView.setText(mFlightsArrayList.get(position).getArrivalAirport().getCode());
        holder.deleteTextView.setOnClickListener(deleteFlightOnClickListener);

        holder.deleteTextView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mFlightsArrayList.size();
    }

    public void update(ArrayList<Flight> flightsArrayList) {
        mFlightsArrayList.clear();
        mFlightsArrayList.addAll(flightsArrayList);
        notifyDataSetChanged();
    }

    public void setDeleteFlightOnClickListener(View.OnClickListener deleteFlightOnClickListener) {
        this.deleteFlightOnClickListener = deleteFlightOnClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.departureIataTextView)
        public TextView departureIataTextView;
        @BindView(R.id.arrivalIataTextView)
        public TextView arrivalIataTextView;
        @BindView(R.id.deleteTextView)
        public TextView deleteTextView;

        public Flight mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
