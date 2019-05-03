package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.OrdersAdapter;
import com.guestlogix.travelercorekit.models.Order;

import java.util.ArrayList;

public class OrdersListFragment extends Fragment {
    private static final String ARG_ORDERS = "arg_orders";

    private RecyclerView ordersRecyclerView;
    private ArrayList<Order> orders;
    private OrdersAdapter ordersAdapter;

    public OrdersListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of OrdersListFragment.
     *
     * @param orders List of orders to show.
     * @return A new instance of {@link OrdersListFragment}.
     */
    public static OrdersListFragment newInstance(ArrayList<Order> orders) {
        OrdersListFragment fragment = new OrdersListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDERS, orders);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orders = (ArrayList<Order>) getArguments().getSerializable(ARG_ORDERS);
            ordersAdapter = new OrdersAdapter(orders, v -> {
                //TODO: Remove the toast
                Toast.makeText(getActivity(), "Item Clicked...", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders_list, container, false);
        ordersRecyclerView = view.findViewById(R.id.recyclerView_ordersFragment_orders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        ordersRecyclerView.setLayoutManager(linearLayoutManager);

        ordersRecyclerView.setAdapter(ordersAdapter);

        return view;
    }
}
