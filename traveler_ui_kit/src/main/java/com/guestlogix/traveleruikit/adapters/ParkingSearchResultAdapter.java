package com.guestlogix.traveleruikit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.callbacks.ParkingSearchCallback;
import com.guestlogix.travelercorekit.models.ParkingItem;
import com.guestlogix.travelercorekit.models.ParkingItemQuery;
import com.guestlogix.travelercorekit.models.ParkingItemSearchResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;

import java.util.HashSet;
import java.util.List;

public class ParkingSearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ParkingSearchCallback {

    private final static String TAG = "ParkingSearchResultAdapter";
    private final static int PARKING_ITEM_VIEW_TYPE = 0;
    private final static int LOADING_VIEW_TYPE = 1;
    private final static int PAGE_SIZE = 10;
    private HashSet<Integer> pagesLoading = new HashSet<>();

    private ParkingItemSearchResult parkingItemSearchResult;
    private OnParkingSearchItemClickListener parkingSearchItemClickListener;
    private ParkingItem selectedParkingItem;

    public ParkingSearchResultAdapter(ParkingItemSearchResult parkingItemSearchResult,
                                      OnParkingSearchItemClickListener parkingSearchItemClickListener) {
        this.parkingItemSearchResult = parkingItemSearchResult;
        this.parkingSearchItemClickListener = parkingSearchItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case PARKING_ITEM_VIEW_TYPE:
                View parkingItemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_parking, parent, false);
                return new ParkingSearchResultViewHolder(parkingItemView);
            case LOADING_VIEW_TYPE:
                View loadingView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading_order, parent, false);
                return new LoadingItemViewHolder(loadingView);
            default:
                throw new IllegalArgumentException(TAG + " unknown viewHolder type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case PARKING_ITEM_VIEW_TYPE:
                ParkingSearchResultViewHolder itemHolder = (ParkingSearchResultViewHolder) holder;
                ParkingItem parkingItem = parkingItemSearchResult.getItems().get(position);

                itemHolder.title.setText(parkingItem.getTitle());
                itemHolder.subtitle.setText(parkingItem.getSubTitle());
                itemHolder.price.setText(parkingItem.getPrice().getLocalizedDescription(TravelerUI.getPreferredCurrency()));
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parkingSearchItemClickListener.onParkingSearchItemClick(parkingItem);
                    }
                });
                int indicatorColorInt = (selectedParkingItem == parkingItem ? R.color.colorPrimary : R.color.off_white);
                @ColorInt int color = ContextCompat.getColor(itemHolder.selectionIndicator.getContext(), indicatorColorInt);
                itemHolder.selectionIndicator.setBackgroundColor(color);
                break;
            case LOADING_VIEW_TYPE:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        List itemsList = parkingItemSearchResult.getItems();
        if (position >= itemsList.size() || itemsList.get(position) == null) {
            this.onFetchItems(position);
            return LOADING_VIEW_TYPE;
        } else {
            return PARKING_ITEM_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return parkingItemSearchResult != null ? parkingItemSearchResult.getTotal() : 0;
    }

    public int setSelectedParkingItem(ParkingItem parkingItem) {
        if (selectedParkingItem != null) {
            int previousIndex = parkingItemSearchResult.getItems().indexOf(selectedParkingItem);
            selectedParkingItem = null;
            notifyItemChanged(previousIndex);
        }
        selectedParkingItem = parkingItem;
        int newIndex = parkingItemSearchResult.getItems().indexOf(selectedParkingItem);
        notifyItemChanged(newIndex);
        return newIndex;
    }

    private void onFetchItems(int position) {
        if (position >= parkingItemSearchResult.getItems().size() || parkingItemSearchResult.getItems().get(position) == null) {
            int page = position / PAGE_SIZE;
            if (pagesLoading.contains(page)) {
                // Disregard if the page is already requested
                return;
            }

            pagesLoading.add(page);
            ParkingItemQuery currentQuery = parkingItemSearchResult.getQuery();
            ParkingItemQuery query = new ParkingItemQuery(
                    currentQuery.getAirportIATA(),
                    currentQuery.getDateRange(),
                    currentQuery.getBoundingBox(),
                    PAGE_SIZE * page,
                    PAGE_SIZE);
            Traveler.searchParkingItems(query, this);
        }
    }

    @Override
    public void onParkingSearchSuccess(ParkingItemSearchResult searchResult) {
        searchResult.merge(searchResult);
        notifyItemRangeChanged(searchResult.getQuery().getOffset(), PAGE_SIZE);
    }

    @Override
    public void onParkingSearchError(Error error) {

    }

    public int getPositionForParkingItem(ParkingItem parkingItem) {
        return parkingItemSearchResult.getItems().indexOf(parkingItem);
    }

    private class ParkingSearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView subtitle;
        private TextView price;
        private View selectionIndicator;

        ParkingSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_parking_title);
            subtitle = itemView.findViewById(R.id.textView_parking_subtitle);
            price = itemView.findViewById(R.id.textView_parking_total);
            selectionIndicator = itemView.findViewById(R.id.view_parking_selected_indicator);
        }
    }

    public interface OnParkingSearchItemClickListener {
        void onParkingSearchItemClick(ParkingItem parkingItem);
    }
}
