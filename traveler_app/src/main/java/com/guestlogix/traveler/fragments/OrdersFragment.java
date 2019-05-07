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
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderResults;

import java.util.List;

public class OrdersFragment extends Fragment {

    private OrdersViewModel ordersViewModel;
    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;

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
        ordersViewModel.getObservableOrdersList().observe(this, this::orderChangeHandler);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        ordersRecyclerView = view.findViewById(R.id.recyclerView_ordersFragment_orders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        ordersRecyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void orderChangeHandler(OrderResults orderResults) {
        if (null == orderResults || null == orderResults.getOrders() || orderResults.getOrders().size() <= 0) {
            return;
        }

        ordersAdapter = new OrdersAdapter(orderResults.getOrders(), v -> {
            //TODO: Remove the toast
            Toast.makeText(getActivity(), "Item Clicked...", Toast.LENGTH_SHORT).show();
        });

        ordersRecyclerView.setAdapter(ordersAdapter);
    }
}
