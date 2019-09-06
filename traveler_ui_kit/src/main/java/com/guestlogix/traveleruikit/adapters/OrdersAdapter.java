package com.guestlogix.traveleruikit.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.*;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FetchOrdersCallback {
    private final static int ORDER_VIEW_TYPE = 0;
    private final static int LOADING_VIEW_TYPE = 1;
    private final static int PAGE_SIZE = 10;

    public interface OnItemClickListener {
        void onOrderClick(Order order);
    }

    private OrderResult result;
    private OrderResult _volatileResult;
    private HashSet pagesLoading = new HashSet();
    private OnItemClickListener onItemClickListener;

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView totalTextView;
        TextView productsTextView;
        TextView cancelledTextView;

        OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.textView_orderItem_dateOrdered);
            totalTextView = itemView.findViewById(R.id.textView_orderItem_totalAmount);
            productsTextView = itemView.findViewById(R.id.textView_orderItem_products);
            cancelledTextView = itemView.findViewById(R.id.textView_orderItem_cancelled);
        }
    }

    class LoadingItemViewHolder extends RecyclerView.ViewHolder {
        LoadingItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public OrdersAdapter(OrderResult result, OnItemClickListener listener) {
        this._volatileResult = this.result = result;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ORDER_VIEW_TYPE:
                View orderView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_order, parent, false);
                return new OrderItemViewHolder(orderView);
            case LOADING_VIEW_TYPE:
                View loadingView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading_order, parent, false);
                return new LoadingItemViewHolder(loadingView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ORDER_VIEW_TYPE:
                OrderItemViewHolder itemHolder = (OrderItemViewHolder) holder;
                Order order = result.getOrders().get(position);
                itemHolder.titleTextView.setText(DateHelper.formatDate(order.getCreatedDate()));
                itemHolder.totalTextView.setText(order.getTotal().getLocalizedDescriptionInBaseCurrency());
                itemHolder.productsTextView.setText(order.getProductTitlesJoinedBy("\n"));
                itemHolder.cancelledTextView.setVisibility(order.getStatus() instanceof OrderStatus.Cancelled ? View.VISIBLE : View.INVISIBLE);
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null)
                            onItemClickListener.onOrderClick(order);
                    }
                });
                break;
            case LOADING_VIEW_TYPE:
                // TODO: Add shimmer effect
                break;
        }

    }

    @Override
    public int getItemCount() {
        return result.getTotal();
    }

    @Override
    public int getItemViewType(int position) {
        if (result.getOrders().get(position) == null) {
            // TODO: Use a predictive method on RecyclerView to better prefetch items
            this.onPrefetchItems(new int[] { position });
            return LOADING_VIEW_TYPE;
        } else {
            return ORDER_VIEW_TYPE;
        }
    }

    // TODO: Remove comment once the Prefetching Recycler View is created
    //@Override
    public void onPrefetchItems(@NonNull int[] positions) {
        for (int i = 0; i < positions.length; i++) {
            // Which `i` needs data
            if (result.getOrders().get(positions[i]) == null) {
                // Calculate the page number
                int page = positions[i] / PAGE_SIZE;

                // Disregard if the page is already requested
                if (pagesLoading.contains(page)) {
                    continue;
                }

                // Otherwise add to pagesLoading and fetch page
                pagesLoading.add(page);

                OrderQuery query = new OrderQuery(PAGE_SIZE * page, PAGE_SIZE, result.getFromDate(), result.getToDate());
                Traveler.fetchOrders(query, page, this);
            }
        }
    }

    // FetchOrdersCallback

    @Override
    public void onOrdersFetchReceive(OrderResult result, int identifier) {
        _volatileResult = result;
    }

    @Override
    public void onOrdersFetchError(Error error, int identifier) {
        pagesLoading.remove(identifier);

        Log.e(this.getClass().getName(), "Error fetching page " + identifier);
    }

    @Override
    public void onOrdersFetchSuccess(OrderResult result, int identifier) {
        this.result = _volatileResult;
        pagesLoading.remove(identifier);
        // TODO: Do a better refresh here. i.e. refresh only cells that are in range?
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public OrderResult getPreviousResult(int identifier) {
        return _volatileResult;
    }

    public void updateOrder(Order order) {
        Iterator iterator = result.getOrders().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Order> pair = (Map.Entry<Integer, Order>) iterator.next();

            if (pair.getValue().equals(order)) {
                result.getOrders().put(pair.getKey(), order);
                return;
            }
        }
    }
}