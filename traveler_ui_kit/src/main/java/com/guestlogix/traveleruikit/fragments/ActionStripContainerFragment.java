package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.ParkingItem;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.ProductType;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.models.BookingContext;

/**
 * Fragment to contain Action strip based on Purchased Strategy
 */
public class ActionStripContainerFragment extends Fragment {
    public static final String ARG_ITEM = "ARG_ITEM";
    public static final String TAG = "ActionStripContainer";

    public static ActionStripContainerFragment newInstance(Product product) {
        ActionStripContainerFragment fragment = new ActionStripContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, product);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_strip_container, container, false);

        if (getArguments() == null || !getArguments().containsKey(ARG_ITEM)) {
            Log.e(TAG, "No CatalogItem in arguments");
            return view;
        }

        Product product = (Product) getArguments().get(ARG_ITEM);

        Fragment fragment;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (product.getProductType() == ProductType.BOOKABLE) {
            fragment = BookableActionStripFragment.newInstance(new BookingContext((BookingItem) product));
        } else if (product.getProductType() == ProductType.PARKING) {
            BookingItem bookingItem = ((ParkingItem) product).toBookingItem();
            fragment = BookableActionStripFragment.newInstance(new BookingContext(bookingItem));
        } else {
            // TODO: This is not done yet
            fragment = new BuyableActionStripFragment();
        }

        transaction.replace(R.id.actionStripContainerFrameLayout, fragment);
        transaction.commit();

        return view;
    }
}
