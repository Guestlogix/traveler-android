package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Arrays;

public class PrefetchRecyclerView extends RecyclerView {
    private PrefetchListener prefetchListener;

    public PrefetchRecyclerView(@NonNull Context context) {
        super(context);
    }

    public PrefetchRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PrefetchRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPrefetchListener(PrefetchListener prefetchListener) {
        this.prefetchListener = prefetchListener;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (dy != 0) {
            LayoutManager mLayoutManager = getLayoutManager();
            int visibleItemsCount;
            int firstCompleteVisibleItemPosition = 0;
            int lastCompleteVisibleItemPosition = 0;
            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                lastCompleteVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
                firstCompleteVisibleItemPosition = getFirstVisibleItem(firstVisibleItemPositions);

            } else if (mLayoutManager instanceof GridLayoutManager) {
                lastCompleteVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                firstCompleteVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                lastCompleteVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                firstCompleteVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            }

            if (null != this.prefetchListener) {

                visibleItemsCount = lastCompleteVisibleItemPosition - firstCompleteVisibleItemPosition;
                int[] indexesToBeFetched = new int[visibleItemsCount * 2];
                int minIndex = 0;
                int maxIndex = null == getAdapter() ? Integer.MAX_VALUE : getAdapter().getItemCount();

                int i = 0;
                for (; i < indexesToBeFetched.length; i++) {
                    int index = (Integer.signum(dy) < 0 ? firstCompleteVisibleItemPosition : lastCompleteVisibleItemPosition) + (Integer.signum(dy)) * (i + 1);
                    if (index < minIndex || index >= maxIndex)
                        break;
                    indexesToBeFetched[i] = index;
                }
                this.prefetchListener.onPrefetchIndexes(Arrays.copyOfRange(indexesToBeFetched, 0, i), this);
            }
        }
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    public int getFirstVisibleItem(int[] firstVisibleItemPositions) {
        int minSize = 0;
        for (int i = 0; i < firstVisibleItemPositions.length; i++) {
            if (i == 0) {
                minSize = firstVisibleItemPositions[i];
            } else if (firstVisibleItemPositions[i] < minSize) {
                minSize = firstVisibleItemPositions[i];
            }
        }
        return minSize;
    }

    public interface PrefetchListener {
        /**
         * Callback to fetch more items
         *
         * @param indexes indexes need to be fetched, always be in sorted ascending order
         * @param view    recyclerView this listener is attached to
         */
        void onPrefetchIndexes(int[] indexes, RecyclerView view);
    }
}