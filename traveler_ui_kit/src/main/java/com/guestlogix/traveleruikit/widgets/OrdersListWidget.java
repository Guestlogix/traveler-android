package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.traveleruikit.adapters.OrdersAdapter;

import java.util.List;

/**
 * A widget to show list of orders.
 */
public class OrdersListWidget extends RecyclerView implements OrdersAdapter.OnOrderItemClickListener {

    private OrdersAdapter ordersAdapter;
    private OrdersAdapter.OnOrderItemClickListener onOrderItemClickListener;

    public OrdersListWidget(@NonNull Context context) {
        super(context);
        initView(context, null, 0, 0);
    }

    public OrdersListWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
    }

    public OrdersListWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle, 0);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (isInEditMode()) {
            return;
        } else {
            ordersAdapter = new OrdersAdapter();
            ordersAdapter.setOnOrderItemClickListener(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            setLayoutManager(layoutManager);
            setAdapter(ordersAdapter);
        }
    }

    public void setOnOrderItemClickListener(OrdersAdapter.OnOrderItemClickListener onOrderItemClickListener) {
        this.onOrderItemClickListener = onOrderItemClickListener;
    }

    public void setOrders(List<Order> orders) {
        ordersAdapter.setOrders(orders);
        ordersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOrderItemClicked(int position) {
        if (null != onOrderItemClickListener) {
            onOrderItemClickListener.onOrderItemClicked(position);
        }
    }
}
