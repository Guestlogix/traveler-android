package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.PurchaseStrategy;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.models.BookingContext;
import com.guestlogix.traveleruikit.models.PurchaseContext;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;

/**
 * Fragment to contain Action strip based on Purchased Strategy
 */
public class ActionStripContainerFragment extends Fragment {
    public static final String ARG_ITEM_DETAILS = "ARG_ITEM_DETAILS";
    public static final String TAG = "ActionStripContainer";

    public static ActionStripContainerFragment newInstance(CatalogItemDetails itemDetails) {
        ActionStripContainerFragment fragment = new ActionStripContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_DETAILS, itemDetails);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_strip_container, container, false);

        if (getArguments() == null || !getArguments().containsKey(ARG_ITEM_DETAILS)) {
            Log.e(TAG, "No CatalogItemDetails in arguments");
            return view;
        }

        CatalogItemDetails itemDetails = (CatalogItemDetails) getArguments().get(ARG_ITEM_DETAILS);

        Fragment fragment;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (itemDetails.getPurchaseStrategy() == PurchaseStrategy.Bookable) {
            fragment = BookableActionStripFragment.newInstance(new BookingContext(itemDetails));
        } else {
            // TODO: This is not done yet
            fragment = new BuyableActionStripFragment();
        }

        transaction.replace(R.id.actionStripContainerFrameLayout, fragment);
        transaction.commit();

        return view;
    }
}
