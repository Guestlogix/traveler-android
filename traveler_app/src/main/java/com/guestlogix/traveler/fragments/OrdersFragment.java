package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.OrdersAdapter;
import com.guestlogix.traveler.viewmodels.OrdersViewModel;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.traveleruikit.widgets.EndlessRecyclerViewScrollListener;

public class OrdersFragment extends Fragment {

    private OrdersViewModel ordersViewModel;
    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private OrderResult orderResult;
    private boolean loading = false;
    private EndlessRecyclerViewScrollListener ordersEndlessRecyclerViewScrollListener;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ordersViewModel = ViewModelProviders.of(getActivity()).get(OrdersViewModel.class);
        ordersViewModel.getObservableOrdersResult().observe(this, this::orderChangeHandler);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        ordersRecyclerView = view.findViewById(R.id.recyclerView_ordersFragment_orders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        ordersRecyclerView.setLayoutManager(linearLayoutManager);
        ordersEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int skip, int totalItemsCount, RecyclerView view) {
                ordersViewModel.fetchOrders(skip, totalItemsCount);
            }

            @Override
            public int getTotalFetchedItems() {
                return orderResult.getOrders().size();
            }

            @Override
            public boolean reloadWindow(int start, int end) {

                for (int i = start; i <= end; i++) {
                    if (null == orderResult.getOrders().get(i)) {
                        return true;
                    }
                }

                return false;
            }
        };
        ordersRecyclerView.addOnScrollListener(ordersEndlessRecyclerViewScrollListener);
        return view;
    }

    private void orderChangeHandler(OrderResult _orderResult) {
        loading = false;
        if (null == _orderResult) {
            return;
        }
        orderResult = _orderResult;

        if (ordersAdapter == null) {
            ordersAdapter = new OrdersAdapter(orderResult, v -> {
                //TODO: Remove the toast
                Toast.makeText(getActivity(), "Item Clicked...", Toast.LENGTH_SHORT).show();
            });
            ordersRecyclerView.setAdapter(ordersAdapter);
        } else {
            ordersAdapter.setOrderResult(orderResult);
            ordersAdapter.notifyDataSetChanged();
        }

    }
}
