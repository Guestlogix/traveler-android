package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.traveleruikit.widgets.OrdersListWidget;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private static final String ARG_ORDERS = "arg_orders";

    private OrdersListWidget ordersListWidget;
    private List<Order> orders;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param orders List of orders to show.
     * @return A new instance of {@link OrdersFragment}.
     */
    public static OrdersFragment newInstance(ArrayList<Order> orders) {
        OrdersFragment fragment = new OrdersFragment();
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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ordersListWidget = view.findViewById(R.id.ordersListWidget_ordersFragment_orders);
        ordersListWidget.setOrders(orders);
        ordersListWidget.setOnOrderItemClickListener(position -> {
            //TODO: Intent for Order Details Activity
            Toast.makeText(getActivity(), "Item Clicked:" + position, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
