package com.guestlogix.traveler.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.adapters.OrdersTabsPagerAdapter;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Traveler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    TabLayout ordersTabs;
    ViewPager ordersPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersTabs = findViewById(R.id.tab_OrdersActivity_ordersTabs);
        ordersPager = findViewById(R.id.viewPager_OrdersActivity_ordersPager);

        fetchOrders();
    }

    //TODO: Updated when filters are decided.
    private void fetchOrders() {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.DAY_OF_YEAR, -105);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.add(Calendar.DAY_OF_YEAR, 105);

        Traveler.fetchOrders(0, 10, fromCalendar.getTime(), toCalendar.getTime(), fetchOrdersCallback);
    }

    FetchOrdersCallback fetchOrdersCallback = new FetchOrdersCallback() {
        @Override
        public void onOrdersFetchSuccess(List<Order> orders) {

            ArrayList<Order> orderArrayList = new ArrayList<>(orders);

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

            OrdersTabsPagerAdapter ordersTabsPagerAdapter = new OrdersTabsPagerAdapter(getSupportFragmentManager(), pastOrdersArrayList, upcomingOrdersArrayList, cancelledOrdersArrayList, OrdersActivity.this);

            ordersPager.setAdapter(ordersTabsPagerAdapter);
            ordersTabs.setupWithViewPager(ordersPager);
        }

        @Override
        public void onOrdersFetchError(Error error) {
            TravelerLog.d("In onOrdersFetchError");
        }
    };
}
