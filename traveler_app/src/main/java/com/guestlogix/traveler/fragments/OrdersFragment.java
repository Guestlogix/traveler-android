package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.OrdersTabsPagerAdapter;
import com.guestlogix.traveler.viewmodels.OrdersViewModel;
import com.guestlogix.travelercorekit.models.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdersFragment extends Fragment {

    private TabLayout ordersTabs;
    private ViewPager ordersPager;

    private OrdersViewModel ordersViewModel;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersViewModel = ViewModelProviders.of(getActivity()).get(OrdersViewModel.class);
        ordersViewModel.getObservableOrdersList().observe(this, this::orderChangeHandler);

        ordersTabs = view.findViewById(R.id.tab_OrdersActivity_ordersTabs);
        ordersPager = view.findViewById(R.id.viewPager_OrdersActivity_ordersPager);

        return view;
    }

    private void orderChangeHandler(List<Order> orderList) {
        if (null == orderList || orderList.size() <= 0) {
            return;
        }
        ArrayList<Order> orderArrayList = new ArrayList<>(orderList);

        ArrayList<Order> pastOrdersArrayList = new ArrayList<>();
        ArrayList<Order> upcomingOrdersArrayList = new ArrayList<>();
        ArrayList<Order> cancelledOrdersArrayList = new ArrayList<>();

        for (Order order : orderArrayList) {

            if (order.getStatus().equalsIgnoreCase("Canceled")) {
                cancelledOrdersArrayList.add(order);
            } else if (order.getCreatedDate().before(new Date())) {
                pastOrdersArrayList.add(order);
            } else {
                upcomingOrdersArrayList.add(order);
            }
        }

        OrdersTabsPagerAdapter ordersTabsPagerAdapter = new OrdersTabsPagerAdapter(getActivity().getSupportFragmentManager(), pastOrdersArrayList, upcomingOrdersArrayList, cancelledOrdersArrayList, getActivity());

        ordersPager.setAdapter(ordersTabsPagerAdapter);
        ordersTabs.setupWithViewPager(ordersPager);
    }
}
