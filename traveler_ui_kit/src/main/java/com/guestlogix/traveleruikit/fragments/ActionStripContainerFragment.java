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
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.models.BookingContext;

/**
 * Fragment to contain Action strip based on Purchased Strategy
 */
public class ActionStripContainerFragment extends Fragment {
    public static final String ARG_ITEM = "ARG_ITEM";
    public static final String TAG = "ActionStripContainer";

    public static ActionStripContainerFragment newInstance(BookingItem bookingItem) {
        ActionStripContainerFragment fragment = new ActionStripContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, bookingItem);
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

        //TODO: remove the whole class and open BookableActionStripFragment directly becasue casting should not be needed
        CatalogItem catalogItem = ((BookingItem) getArguments().get(ARG_ITEM));


        Fragment fragment;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

//        if (catalogItem.getProductType() == ProductType.BOOKABLE) {
            fragment = BookableActionStripFragment.newInstance(new BookingContext((BookingItem) catalogItem));
//        } else {
//            fragment = new BuyableActionStripFragment();
//        }

        transaction.replace(R.id.actionStripContainerFrameLayout, fragment);
        transaction.commit();

        return view;
    }
}
