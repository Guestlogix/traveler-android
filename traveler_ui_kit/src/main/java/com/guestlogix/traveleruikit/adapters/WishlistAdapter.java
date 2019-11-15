package com.guestlogix.traveleruikit.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.callbacks.WishlistFetchCallback;
import com.guestlogix.travelercorekit.callbacks.WishlistRemoveCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static com.guestlogix.traveleruikit.fragments.WishlistFragment.PAGE_SIZE;

public class WishlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements WishlistFetchCallback {
    private final static String TAG = "WishlistAdapter";
    private final static int WISHLISTED_ITEM_VIEW_TYPE = 0;
    private final static int LOADING_VIEW_TYPE = 1;
    private final static int ALPHA_TRANSPARENT = 85;
    private final static int ALPHA_OPAQUE = 255;
    private final static int IMAGE_FADE_IN_DURATION_MS = 100;

    public interface OnItemClickListener {
        void onWishlistedItemClick(BookingItem bookingItem);
    }

    private WishlistResult result;
    private HashSet<Integer> pagesLoading = new HashSet<>();
    private OnItemClickListener onItemClickListener;

    class WishlistedItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView notAvailableTextView;
        TextView titleTextView;
        TextView subtitleTextView;
        TextView priceTextView;
        TextView removeTextView;

        WishlistedItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_saveditem_image);
            notAvailableTextView = itemView.findViewById(R.id.textView_saveditem_notavailable);
            titleTextView = itemView.findViewById(R.id.textView_savedItem_title);
            subtitleTextView = itemView.findViewById(R.id.textView_savedItem_subtitle);
            priceTextView = itemView.findViewById(R.id.textView_savedItem_price);
            removeTextView = itemView.findViewById(R.id.textView_savedItem_remove);
        }
    }

    class LoadingItemViewHolder extends RecyclerView.ViewHolder {
        LoadingItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public WishlistAdapter(OnItemClickListener listener) {
        this.onItemClickListener = listener;
        initialFetch();
    }

    private void initialFetch() {
        WishlistQuery query = new WishlistQuery(0, PAGE_SIZE, null, new Date());
        int page = 0;
        pagesLoading.add(page);
        Traveler.fetchWishlist(query, page, this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case WISHLISTED_ITEM_VIEW_TYPE:
                View orderView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_wishisted_item, parent, false);
                return new WishlistedItemViewHolder(orderView);
            case LOADING_VIEW_TYPE:
                View loadingView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading_order, parent, false);
                return new LoadingItemViewHolder(loadingView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case WISHLISTED_ITEM_VIEW_TYPE:
                WishlistedItemViewHolder itemHolder = (WishlistedItemViewHolder) holder;
                BookingItem bookingItem = result.getItems().get(position);

                itemHolder.titleTextView.setText(bookingItem.getTitle());
                itemHolder.subtitleTextView.setText(bookingItem.getSubtitle());

                Resources resources = itemHolder.titleTextView.getResources();
                if (bookingItem.getItemResource().isAvailable()) {
                    String localizedPrice = String.format(
                            Locale.getDefault(),
                            resources.getString(R.string.label_price_per_person),
                            bookingItem.getItemResource().getPrice().getLocalizedDescription(TravelerUI.getPreferredCurrency()));
                    itemHolder.priceTextView.setText(localizedPrice);
                    itemHolder.notAvailableTextView.setVisibility(View.GONE);
                    itemHolder.imageView.setImageAlpha(ALPHA_OPAQUE);
                } else {
                    // If item is not available its price will be invalid
                    itemHolder.priceTextView.setText("");
                    itemHolder.notAvailableTextView.setVisibility(View.VISIBLE);
                    itemHolder.imageView.setImageAlpha(ALPHA_TRANSPARENT);
                }

                itemHolder.imageView.setImageBitmap(null);
                AssetManager.getInstance().loadImage(
                        bookingItem.getImageUrl(),
                        (int) resources.getDimension(R.dimen.thumbnail_width),
                        (int) resources.getDimension(R.dimen.thumbnail_height),
                        holder.hashCode(),
                        new ImageLoader.ImageLoaderCallback() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap) {
                                itemHolder.imageView.setImageBitmap(bitmap);
                                AlphaAnimation animation = new AlphaAnimation(0, 1);
                                animation.setDuration(IMAGE_FADE_IN_DURATION_MS);
                                itemHolder.imageView.startAnimation(animation);
                            }

                            @Override
                            public void onError() {
                                // Do nothing.
                            }
                        });

                itemHolder.removeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(itemHolder.removeTextView.getContext())
                                .setMessage("Are you sure you want to remove this item?")
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeItemFromWishlist(bookingItem, view.getContext());
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    }
                });

                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onWishlistedItemClick(bookingItem);
                        }
                    }
                });
                break;
            case LOADING_VIEW_TYPE:
                break;
        }

    }

    /**
     * Remove the item from the adapter, then calls API to remove the item. If onWishlistRemoveError is called,
     * the removed item will be reinstated
     * @param bookingItem the item to be removed from Adapter and API
     * @param context context with which to create error dialogs
     */
    private void removeItemFromWishlist(BookingItem bookingItem, Context context) {
        int indexRemove = removeWishlistedItemFromAdapterById(bookingItem.getItemResource().getId());
        Traveler.wishlistRemove(bookingItem.getItemResource(), result, new WishlistRemoveCallback() {
            @Override
            public void onWishlistRemoveSuccess(Product item, CatalogItemDetails itemDetails) {
                // no-op
            }

            @Override
            public void onWishlistRemoveError(Error error, @Nullable WishlistResult result) {
                addWishlistedItemToAdapter(bookingItem, indexRemove);
                new AlertDialog.Builder(context)
                        .setMessage(error.getMessage())
                        .setNeutralButton("OK", null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return result != null ? result.getTotal() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        List itemsList = result.getItems();
        if (position >= itemsList.size() || itemsList.get(position) == null) {
            this.onFetchItems(position);
            return LOADING_VIEW_TYPE;
        } else {
            return WISHLISTED_ITEM_VIEW_TYPE;
        }
    }

    private void onFetchItems(int position) {
        if (position >= result.getItems().size() || result.getItems().get(position) == null) {
            int page = position / PAGE_SIZE;
            if (pagesLoading.contains(page)) {
                // Disregard if the page is already requested
                return;
            }

            pagesLoading.add(page);
            WishlistQuery query = new WishlistQuery(PAGE_SIZE * page, PAGE_SIZE, result.getFromDate(), result.getToDate());
            Traveler.fetchWishlist(query, page, this);
        }
    }

    @Override
    public void onWishlistFetchReceive(WishlistResult result, int identifier) {
        this.result = result;
        pagesLoading.remove(identifier);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeChanged(identifier * PAGE_SIZE, PAGE_SIZE);
            }
        });
    }

    @Override
    public void onWishlistFetchError(Error error, int identifier) {
        pagesLoading.remove(identifier);
        Log.e(TAG, "Error fetching page " + identifier);
        error.printStackTrace();
    }

    @Override
    public void onWishlistFetchSuccess(WishlistResult result, int identifier) {
    }

    @Nullable
    @Override
    public WishlistResult getPreviousResult() {
        return result;
    }

    /**
     * Call this method after a successful API call to remove an item from the recyclerView adapter
     * @param productId the item that was removed from wishlist via API
     * @return index at which the item was removed from; -1 if not found.
     */
    public int removeWishlistedItemFromAdapterById(String productId) {
        int index = result.remove(productId);
        if (index != -1) {
            notifyItemRemoved(index);
        }
        return index;
    }

    /**
     * @param bookingItem item to be added
     * @param index index that the item shall be added at
     */
    private void addWishlistedItemToAdapter(BookingItem bookingItem, int index) {
        result.add(bookingItem, index);
        notifyItemInserted(index);
    }
}