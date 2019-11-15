package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.traveleruikit.R;

/**
 * Fragment to contain Action strip based on Purchased Strategy
 */
public class ActionStripContainerFragment extends Fragment {
    public static final String ARG_Product = "ARG_Product";
    public static final String TAG = "ActionStripContainer";

    public static ActionStripContainerFragment newInstance(Product product) {
        ActionStripContainerFragment fragment = new ActionStripContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_Product, product);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_strip_container, container, false);

        if (getArguments() == null || !getArguments().containsKey(ARG_Product)) {
            Log.e(TAG, "No CatalogItem in arguments");
            return view;
        }

        //TODO: remove the whole class and open BookableActionStripFragment directly because casting should not be needed
        Product product = ((Product) getArguments().get(ARG_Product));


        Fragment fragment;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        fragment = PurchaseActionStripFragment.newInstance(product);

        transaction.replace(R.id.actionStripContainerFrameLayout, fragment);
        transaction.commit();

        return view;
    }
}
