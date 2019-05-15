package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.OrdersAdapter;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.widgets.EndlessRecyclerView;
import com.guestlogix.traveleruikit.widgets.EndlessRecyclerView.PrefetchListener;

import java.util.HashMap;

public class OrdersFragment extends Fragment {

    private EndlessRecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private OrderResult orderResult;
    private boolean loading = false;
    private OrderQuery orderQuery;

    private static String ARG_ORDER_RESULT = "arg_order_result";
    private static String ARG_ORDER_QUERY = "arg_order_query";

    public OrdersFragment() {
        // Do nothing.
    }

    public static OrdersFragment newInstance(OrderResult orderResult, OrderQuery orderQuery) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER_RESULT, orderResult);
        args.putSerializable(ARG_ORDER_QUERY, orderQuery);
        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();

        if (null != args && args.containsKey(ARG_ORDER_RESULT) && args.containsKey(ARG_ORDER_QUERY)) {
            orderResult = (OrderResult) args.getSerializable(ARG_ORDER_RESULT);
            orderQuery = (OrderQuery) args.getSerializable(ARG_ORDER_QUERY);
        } else {
            throw new RuntimeException("ARG_ORDER_RESULT and ARG_ORDER_QUERY are required to show the orders");
        }

        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        ordersRecyclerView = view.findViewById(R.id.recyclerView_ordersFragment_orders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        ordersRecyclerView.setLayoutManager(linearLayoutManager);
        ordersRecyclerView.setPrefetchListener(prefetchListener);

        ordersAdapter = new OrdersAdapter(orderResult, v -> {
            //TODO: Remove the toast
            Toast.makeText(getActivity(), "Item Clicked...", Toast.LENGTH_SHORT).show();
        });
        ordersRecyclerView.setAdapter(ordersAdapter);

        return view;
    }

    private PrefetchListener prefetchListener = (indexes, view) -> {

        if (!loading)
            for (int i : indexes) {
                if (null == orderResult.getOrders().get(i)) {
                    loading = false;
                    fetchOrders(i, indexes.length);
                    break;
                }
            }
    };

    private void orderChangeHandler(OrderResult orderResult) {
        loading = false;
        if (null == orderResult) {
            return;
        }
        this.orderResult = orderResult;

        ordersAdapter.setOrderResult(this.orderResult);
        ordersAdapter.notifyDataSetChanged();
    }

    private void fetchOrders(Integer skip, Integer take) {

        orderQuery.setSkip(skip);
        orderQuery.setTake(take);
        Traveler.fetchOrders(orderQuery, fetchOrdersCallback);
    }

    private FetchOrdersCallback fetchOrdersCallback = new FetchOrdersCallback() {
        @Override
        public void onOrdersFetchSuccess(OrderResult orders) {
            orderChangeHandler(orders);
        }

        @Override
        public void onOrderResultsFetched(OrderResult orders) {

        }

        @Override
        public OrderResult getPreviousOrderResults() {
            return orderResult;
        }

        @Override
        public void onOrdersFetchError(Error error) {
        }
    };
}
