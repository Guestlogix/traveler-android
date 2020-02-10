package com.guestlogix.traveleruikit.itinerary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.ItineraryItem;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int ORDER_VIEW_TYPE = 0;
    private final static int LOADING_VIEW_TYPE = 1;
    private final static int PAGE_SIZE = 10;

    private List<ItineraryItem> itineraryItems;
    private OnItemClickListener onItemClickListener;

    public ItineraryAdapter(List<ItineraryItem> itineraryItems, OnItemClickListener listener) {
        this.itineraryItems = itineraryItems;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_itinerary, parent, false);
        return new OrderItemViewHolder(orderView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderItemViewHolder itemHolder = (OrderItemViewHolder) holder;
        ItineraryItem itinerary = itineraryItems.get(position);

        itemHolder.tvTitle.setText(itinerary.getTitle());
        itemHolder.tvSubtitle.setText(itinerary.getSubtitle());
        itemHolder.tvTime.setText(DateHelper.formatToHourMinuteMeridian(itinerary.getStartDate()));
        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(itinerary);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itineraryItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(ItineraryItem itineraryItem);
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTitle, tvSubtitle;

        OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
        }
    }
}