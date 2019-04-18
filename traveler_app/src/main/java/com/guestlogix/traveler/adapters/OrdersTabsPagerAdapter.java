package com.guestlogix.traveler.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.guestlogix.traveler.R;
import com.guestlogix.travelercorekit.models.Order;

import com.guestlogix.traveleruikit.fragments.OrdersFragment;

import java.util.ArrayList;

public class OrdersTabsPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Order> pastOrders;
    private ArrayList<Order> upcomingOrders;
    private ArrayList<Order> cancelledOrders;
    private Context context;

    public OrdersTabsPagerAdapter(FragmentManager fm, ArrayList<Order> pastOrders, ArrayList<Order> upcomingOrders, ArrayList<Order> cancelledOrders, Context context) {
        super(fm);
        this.context = context;
        this.pastOrders = pastOrders;
        this.upcomingOrders = upcomingOrders;
        this.cancelledOrders = cancelledOrders;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OrdersFragment.newInstance(upcomingOrders);
            case 1:
                return OrdersFragment.newInstance(pastOrders);
            case 2:
                return OrdersFragment.newInstance(cancelledOrders);

        }
        return OrdersFragment.newInstance(upcomingOrders);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.upcoming);
            case 1:
                return context.getString(R.string.past);
            case 2:
                return context.getString(R.string.cancelled);
            default:
                return null;
        }
    }
}
