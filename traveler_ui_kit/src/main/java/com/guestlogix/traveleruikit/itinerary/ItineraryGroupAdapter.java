package com.guestlogix.traveleruikit.itinerary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;

import java.util.List;

public class ItineraryGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItineraryGroup> dateGroupedItineraries;
    private Context context;
    ItineraryAdapter.OnItemClickListener itineraryClickCallback;

    public ItineraryGroupAdapter(Context context, List<ItineraryGroup> dateGroupedItineraries, ItineraryAdapter.OnItemClickListener itineraryClickCallback) {
        this.context = context;
        this.dateGroupedItineraries = dateGroupedItineraries;
        this.itineraryClickCallback = itineraryClickCallback;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itinerariesItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_itineraries_group, parent, false);
        return new ItinerariesGroupViewHolder(itinerariesItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItinerariesGroupViewHolder itemHolder = (ItinerariesGroupViewHolder) holder;
        ItineraryGroup item = dateGroupedItineraries.get(position);

        itemHolder.tvTitle.setText(DateHelper.formatToDayNameMonthDayYear(item.getGroupDate()));
        itemHolder.rvItineraries.setAdapter(new ItineraryAdapter(item.getItineraries(), itineraryClickCallback));
        itemHolder.rvItineraries.setLayoutManager(new LinearLayoutManager(context));
    }


    @Override
    public int getItemCount() {
        return dateGroupedItineraries.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    class ItinerariesGroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RecyclerView rvItineraries;


        ItinerariesGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            rvItineraries = itemView.findViewById(R.id.rvItineraries);
            rvItineraries.setNestedScrollingEnabled(false);
        }
    }

}