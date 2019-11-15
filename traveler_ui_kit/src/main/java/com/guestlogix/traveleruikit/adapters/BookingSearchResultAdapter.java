package com.guestlogix.traveleruikit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.callbacks.BookingSearchCallback;
import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.BookingItemQuery;
import com.guestlogix.travelercorekit.models.BookingItemSearchResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;

import java.util.HashSet;
import java.util.List;

import static com.guestlogix.travelercorekit.models.BookingItemQuery.DEFAULT_PAGE_SIZE;

public class BookingSearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements BookingSearchCallback {

    private final static int BOOKING_ITEM_VIEW_TYPE = 0;
    private final static int LOADING_VIEW_TYPE = 1;
    private HashSet<Integer> pagesLoading = new HashSet<>();
    private BookingItemSearchResult result;
    private OnBookingSearchItemClickListener onBookingSearchItemClickListener;
    private Context context;

    public BookingSearchResultAdapter(
            BookingItemSearchResult result,
            OnBookingSearchItemClickListener onBookingSearchItemClickListener,
            Context context) {
        this.result = result;
        this.onBookingSearchItemClickListener = onBookingSearchItemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case BOOKING_ITEM_VIEW_TYPE:
                View orderView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_booking_search_result, parent, false);
                return new BookingSearchResultViewHolder(orderView);
            case LOADING_VIEW_TYPE:
                View loadingView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading_order, parent, false);
                return new LoadingItemViewHolder(loadingView);
            default:
                throw new IllegalStateException("view type unknown");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case BOOKING_ITEM_VIEW_TYPE:
                BookingSearchResultViewHolder itemHolder = (BookingSearchResultViewHolder) holder;
                BookingItem bookingItem = result.getItems().get(position);

                itemHolder.thumbnail.setImageResource(R.color.colorPrimary);
                AssetManager.getInstance().loadImage(
                        bookingItem.getImageUrl(),
                        itemHolder.thumbnail.getWidth(),
                        itemHolder.thumbnail.getHeight(),
                        itemHolder.thumbnail.hashCode(),
                        new ImageLoader.ImageLoaderCallback() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap) {
                                itemHolder.thumbnail.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onError() {
                                // Do nothing.
                            }
                        });

                itemHolder.title.setText(bookingItem.getTitle());
                itemHolder.price.setText(String.valueOf(bookingItem.getItemResource().getPrice().getLocalizedDescription(TravelerUI.getPreferredCurrency())));
                itemHolder.itemView.setOnClickListener(v -> {

                    if (onBookingSearchItemClickListener != null) {
                        onBookingSearchItemClickListener.onBookingSearchItemClick(result.getItems().get(position));
                    }
                });
                break;
            case LOADING_VIEW_TYPE:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        List itemsList = result.getItems();
        if (position >= itemsList.size() || itemsList.get(position) == null) {
            this.onFetchItems(position);
            return LOADING_VIEW_TYPE;
        } else {
            return BOOKING_ITEM_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return result != null ? result.getTotal() : 0;
    }

    private void onFetchItems(int position) {
        if (position >= result.getItems().size() || result.getItems().get(position) == null) {
            int page = position / DEFAULT_PAGE_SIZE;
            if (pagesLoading.contains(page)) {
                // Disregard if the page is already requested
                return;
            }

            pagesLoading.add(page);
            BookingItemQuery currentQuery = result.getQuery();
            BookingItemQuery query = new BookingItemQuery(currentQuery.getQueryText(),
                    currentQuery.getCity(),
                    currentQuery.getPriceRangeFilter(),
                    currentQuery.getCategories(),
                    currentQuery.getBoundingBox(),
                    currentQuery.getBookingItemSort(),
                    DEFAULT_PAGE_SIZE * page,
                    DEFAULT_PAGE_SIZE);
            Traveler.searchBookingItems(query, this);
        }
    }

    @Override
    public void onBookingSearchSuccess(BookingItemSearchResult searchResult) {
        result.merge(searchResult);
        notifyItemRangeChanged(searchResult.getQuery().getOffset(), DEFAULT_PAGE_SIZE);
    }

    @Override
    public void onBookingSearchError(Error error) {

    }

    public class BookingSearchResultViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView title;
        public TextView price;

        public BookingSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.booking_search_item_thumbnail);
            title = itemView.findViewById(R.id.booking_search_item_title);
            price = itemView.findViewById(R.id.booking_search_item_price);
        }
    }

    public interface OnBookingSearchItemClickListener {
        void onBookingSearchItemClick(BookingItem bookingItem);
    }
}
