package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class EndlessRecyclerView extends RecyclerView {
    private PrefetchListener prefetchListener;

    public EndlessRecyclerView(@NonNull Context context) {
        super(context);
    }

    public EndlessRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EndlessRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPrefetchListener(PrefetchListener prefetchListener) {
        this.prefetchListener = prefetchListener;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        LayoutManager mLayoutManager = getLayoutManager();
        int visibleItemsCount;
        int firstCompleteVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        int lastCompleteVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        if (mLayoutManager instanceof LinearLayoutManager) {
            visibleItemsCount = lastCompleteVisibleItemPosition - firstCompleteVisibleItemPosition;
        } else {
            throw new RuntimeException("Only LinearLayoutManager is handled by EndlessRecyclerViewScrollListener");
        }

        if (null != this.prefetchListener) {

            int[] indexesToBeFetched = new int[visibleItemsCount * 2];

            int i = 0;
            for (; i < indexesToBeFetched.length; i++) {
                int index = (Integer.signum(dy) < 0 ? firstCompleteVisibleItemPosition : lastCompleteVisibleItemPosition) + (Integer.signum(dy)) * (i + 1);
                if (index < 0)
                    break;
                indexesToBeFetched[i] = index;
            }
            this.prefetchListener.onPrefetchIndexes(Arrays.copyOfRange(indexesToBeFetched, 0, i), this);
        }

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
