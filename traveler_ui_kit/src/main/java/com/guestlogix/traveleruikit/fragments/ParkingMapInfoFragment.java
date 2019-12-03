package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.ParkingItemSearchResult;
import com.guestlogix.traveleruikit.R;

public class ParkingMapInfoFragment extends Fragment {
    private static String TAG = "BookingItemDetailsFragment";
    static String ARG_PARKING_SEARCH_RESULT = "parking_search_result";

    public static ParkingMapInfoFragment newInstance(ParkingItemSearchResult parkingItemSearchResult) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARKING_SEARCH_RESULT, parkingItemSearchResult);
        ParkingMapInfoFragment fragment = new ParkingMapInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_item_details, container, false);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_PARKING_SEARCH_RESULT)) {
            Log.e(TAG, "No ParkingItemSearchResult in arguments");
            return null;
        }

        ParkingItemSearchResult parkingItemSearchResult = (ParkingItemSearchResult) args.get(ARG_PARKING_SEARCH_RESULT);

        if (parkingItemSearchResult == null) {
            Log.e(TAG, "No CatalogItemDetails");
            return view;
        }

        return view;
    }

}
