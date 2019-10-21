package com.guestlogix.traveleruikit.adapters;

import android.app.AlertDialog;
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
        void onWishlistedItemClick(CatalogItem catalogItemDetails);
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
                CatalogItem catalogItem = result.getItems().get(position);

                itemHolder.titleTextView.setText(catalogItem.getTitle());
                itemHolder.subtitleTextView.setText(catalogItem.getSubTitle());

                Resources resources = itemHolder.titleTextView.getResources();
                if (catalogItem.isAvailable()) {
                    String localizedPrice = String.format(
                            Locale.getDefault(),
                            resources.getString(R.string.label_price_per_person),
                            catalogItem.getPrice().getLocalizedDescription(TravelerUI.getPreferredCurrency()));
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
                        catalogItem.getImageURL(),
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
                    public void onClick(View v) {
                        new AlertDialog.Builder(itemHolder.removeTextView.getContext())
                                .setMessage("Are you sure you want to remove this item?")
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeItemFromWishlist(catalogItem);
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
                            onItemClickListener.onWishlistedItemClick(catalogItem);
                        }
                    }
                });
                break;
            case LOADING_VIEW_TYPE:
                break;
        }

    }

    private void removeItemFromWishlist(CatalogItem catalogItem) {
        Traveler.wishlistRemove(catalogItem, result, new WishlistRemoveCallback() {
            @Override
            public void onWishlistRemoveSuccess(Product item, CatalogItemDetails itemDetails) {

            }

            @Override
            public void onWishlistRemoveError(Error error, @Nullable WishlistResult result) {

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
        // TODO: Do a better refresh here. i.e. refresh only cells that are in range?
        // TODO ALVTAG use diffUtil
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
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

    //TODO ALVTAG this is for when an item is removed from wishlist using CatalogItemDetailsFragment
    public void removeWishlistedItem(CatalogItem itemToRemove) {
        for (CatalogItem catalogItem : result.getItems()) {

            if (itemToRemove.getId().equals(catalogItem.getId())) {
                result.getItems().remove(itemToRemove);
                return;
            }
        }
    }
}