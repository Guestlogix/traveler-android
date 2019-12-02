package com.guestlogix.traveleruikit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.BookingItemSearchResult;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.BookingItemDetailsActivity;
import com.guestlogix.traveleruikit.adapters.BookingSearchResultAdapter;

public class BookingSearchResultFragment extends Fragment
        implements BookingSearchResultAdapter.OnBookingSearchItemClickListener {
    static public String ARG_BOOKING_ITEM_SEARCH_RESULTS = "ARG_BOOKING_ITEM_SEARCH_RESULTS";
    static private String TAG = "BookingSearchResultFragment";

    public static BookingSearchResultFragment newInstance(BookingItemSearchResult result) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOKING_ITEM_SEARCH_RESULTS, result);
        BookingSearchResultFragment fragment = new BookingSearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_search_result, container, false);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_BOOKING_ITEM_SEARCH_RESULTS)) {
            Log.e(TAG, "No CatalogItemDetails in arguments");
            return null;
        }

        BookingItemSearchResult result = (BookingItemSearchResult) args.get(ARG_BOOKING_ITEM_SEARCH_RESULTS);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_booking_search_results);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter adapter = new BookingSearchResultAdapter(result, this,  getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onBookingSearchItemClick(BookingItem bookingItem) {
        Intent intent = new Intent(this.getContext(), BookingItemDetailsActivity.class);
        intent.putExtra(BookingItemDetailsActivity.ARG_PRODUCT, bookingItem);
        startActivity(intent);
    }
}
