package com.guestlogix.traveleruikit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.Locale;

public class ParkingSearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ParkingSearchCallback {

    private final static String TAG = "ParkingSearchResultAdapter";
    private final static int PARKING_ITEM_VIEW_TYPE = 0;
    private final static int LOADING_VIEW_TYPE = 1;
    private final static int HEADER_VIEW_TYPE = 2;
    private final static int FOOTER_VIEW_TYPE = 3;
    private final static int PAGE_SIZE = 10;
    private HashSet<Integer> pagesLoading = new HashSet<>();
    private int layoutId;

    private ParkingItemSearchResult parkingItemSearchResult;
    private OnParkingSearchItemClickListener parkingSearchItemClickListener;
    private ParkingItem selectedParkingItem;
    private int orientation;

    /**
     * @param orientation LinearLayoutManager.VERTICAL or LinearLayoutManager.HORIZONTAL
     */
    public ParkingSearchResultAdapter(ParkingItemSearchResult parkingItemSearchResult,
                                      OnParkingSearchItemClickListener parkingSearchItemClickListener,
                                      int orientation) {
        this.parkingItemSearchResult = parkingItemSearchResult;
        this.parkingSearchItemClickListener = parkingSearchItemClickListener;
        this.orientation = orientation;
        switch (orientation) {
            case LinearLayoutManager.VERTICAL:
                this.layoutId = R.layout.item_parking_vertical;
                break;
            case LinearLayoutManager.HORIZONTAL:
                this.layoutId = R.layout.item_parking_horizontal;
                break;
            default:
                throw new IllegalArgumentException(TAG + ": invalid orientation");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case PARKING_ITEM_VIEW_TYPE:
                View parkingItemView = LayoutInflater.from(parent.getContext())
                        .inflate(layoutId, parent, false);
                return new ParkingSearchResultViewHolder(parkingItemView);
            case LOADING_VIEW_TYPE:
                View loadingView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading_order, parent, false);
                return new LoadingItemViewHolder(loadingView);
            case HEADER_VIEW_TYPE:
                View headerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_parking_vertical_header, parent, false);
                return new HeaderViewHolder(headerView);
            case FOOTER_VIEW_TYPE:
                View footerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_parking_horizontal_footer, parent, false);
                return new FooterViewHolder(footerView);
            default:
                throw new IllegalArgumentException(TAG + " unknown viewHolder type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case PARKING_ITEM_VIEW_TYPE:
                if (orientation == LinearLayoutManager.VERTICAL) {
                    // vertical carousel has a header view; posn 1 on recyclerview is mapped to itemIndex 0
                    position -= 1;
                }
                ParkingSearchResultViewHolder itemHolder = (ParkingSearchResultViewHolder) holder;
                ParkingItem parkingItem = parkingItemSearchResult.getItems().get(position);

                itemHolder.title.setText(parkingItem.getTitle());
                itemHolder.subtitle.setText(parkingItem.getSubTitle());
                itemHolder.price.setText(parkingItem.getPrice().getValueWithBaseCurrencySuffix());
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parkingSearchItemClickListener.onParkingItemViewHolderClick(parkingItem);
                    }
                });
                int indicatorColorInt = (selectedParkingItem == parkingItem ? R.color.colorPrimary : R.color.off_white);
                @ColorInt int color = ContextCompat.getColor(itemHolder.selectionIndicator.getContext(), indicatorColorInt);
                itemHolder.selectionIndicator.setBackgroundColor(color);

                itemHolder.viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parkingSearchItemClickListener.onParkingItemDetailsClick(parkingItem);
                    }
                });
                break;
            case HEADER_VIEW_TYPE:
                HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                headerHolder.textView.setText(String.format(Locale.getDefault(), "%d Parking Lots Found", parkingItemSearchResult.getItems().size()));
                break;
            case FOOTER_VIEW_TYPE:
            case LOADING_VIEW_TYPE:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        List itemsList = parkingItemSearchResult.getItems();
        if (orientation == LinearLayoutManager.VERTICAL) {
            if (position == 0) {
                return HEADER_VIEW_TYPE;
            } else {
                int reindexedPosition = position - 1;
                if (reindexedPosition >= itemsList.size() || itemsList.get(reindexedPosition) == null) {
                    this.onFetchItems(reindexedPosition);
                    return LOADING_VIEW_TYPE;
                } else {
                    return PARKING_ITEM_VIEW_TYPE;
                }
            }

        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (position >= itemsList.size()) {
                return FOOTER_VIEW_TYPE;
            } else {
                if (itemsList.get(position) == null) {
                    this.onFetchItems(position);
                    return LOADING_VIEW_TYPE;
                } else {
                    return PARKING_ITEM_VIEW_TYPE;
                }
            }
        }
        throw new IllegalArgumentException("unknown item type at position:" + position +
                " in orientation:" + orientation);
    }

    @Override
    public int getItemCount() {
        //adds one for header (vertical "23 parking lots found") or footer (horizontal "search more")
        return 1 + (parkingItemSearchResult != null ? parkingItemSearchResult.getTotal() : 0);
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

    public void setData(ParkingItemSearchResult searchResult) {
        parkingItemSearchResult = searchResult;
    }

    private class ParkingSearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView subtitle;
        private TextView price;
        private View selectionIndicator;
        private View viewButton;

        ParkingSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_parking_title);
            subtitle = itemView.findViewById(R.id.textView_parking_subtitle);
            price = itemView.findViewById(R.id.textView_parking_total);
            selectionIndicator = itemView.findViewById(R.id.view_parking_selected_indicator);
            viewButton = itemView.findViewById(R.id.textView_parking_view_label);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView_parking_lot_count);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnParkingSearchItemClickListener {

        void onParkingItemViewHolderClick(ParkingItem parkingItem);

        void onParkingItemDetailsClick(ParkingItem parkingItem);

    }
}
