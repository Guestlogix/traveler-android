package com.guestlogix.traveleruikit.widgets;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ScrollListener for RecyclerView to provide callbacks to fetch items endlessly.
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 2;
    private int pageSize = 10;
    private int skipItems = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingItemIndex = 0;
    private boolean hasConsumed = false;

    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Create an instance of EndlessScrollListener and add it to recyclerViewScrollListeners.
     * you will get onLoadMore() callback with items to skip and number of items to load.
     *
     * @param layoutManager currently only LinearLayoutManager is supported.
     */
    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition;
        int totalItemCount = getTotalFetchedItems();
        if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else {
            throw new RuntimeException("Only LinearLayoutManager is handled by EndlessRecyclerViewScrollListener");
        }

        if (totalItemCount < previousTotalItemCount) {
            this.skipItems = this.startingItemIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && ((lastVisibleItemPosition + visibleThreshold) > totalItemCount) && reloadWindow(totalItemCount - 1, totalItemCount - 1 + pageSize)) {
            skipItems = totalItemCount - 1;
            onLoadMore(skipItems, totalItemCount, view);
            loading = true;
        }
    }

    /**
     * Whenever user resets the recyclerView e.g. pulls to refresh
     */
    public void resetState() {
        this.skipItems = this.startingItemIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    /**
     * Callback to fetch more items
     *
     * @param skip            number of items to skip
     * @param totalItemsCount total number of items to be fetched beyond skipped
     * @param view            recyclerView this listener is attached to
     */
    public abstract void onLoadMore(int skip, int totalItemsCount, RecyclerView view);

    /**
     * Callback to get the total number of items we already have fetched
     *
     * @return total number existing items.
     */
    public abstract int getTotalFetchedItems();

    /**
     * @param start starting index of window to check items
     * @param end   last index of window to check items
     * @return whether we want to reload this window
     */
    public abstract boolean reloadWindow(int start, int end);

}