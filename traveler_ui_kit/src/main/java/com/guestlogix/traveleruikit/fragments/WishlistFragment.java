package com.guestlogix.traveleruikit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.BookingItemDetails;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.CatalogItemDetailsActivity;
import com.guestlogix.traveleruikit.adapters.WishlistAdapter;

public class WishlistFragment extends Fragment implements WishlistAdapter.OnItemClickListener{
    public static final int PAGE_SIZE = 10;
    private static final int REQUEST_CODE_ITEM_DETAILS = 1;
    private static final String TAG = "WishlistFragment";
    private WishlistAdapter adapter;

    public WishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WishlistFragment.
     */
    public static WishlistFragment newInstance() {
        return new WishlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_ordersFragment_orders);
        adapter = new WishlistAdapter(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onWishlistedItemClick(BookingItem bookingItem) {
        Intent intent = new Intent(this.getContext(), CatalogItemDetailsActivity.class);
        intent.putExtra(CatalogItemDetailsActivity.ARG_PRODUCT, bookingItem);
        startActivityForResult(intent, REQUEST_CODE_ITEM_DETAILS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ITEM_DETAILS && null != data && null != data.getExtras()) {
            Object object = data.getExtras().get(CatalogItemDetailsActivity.ARG_PRODUCT);
            boolean isWishlisted = false;
            String id = null;
            if (object instanceof BookingItemDetails) {
                BookingItemDetails bookingItemDetails = ((BookingItemDetails) object);
                isWishlisted = bookingItemDetails.isWishlisted();
                id = bookingItemDetails.getId();
            } else if (object instanceof BookingItem) {
                BookingItem bookingItem = ((BookingItem) object);
                isWishlisted = bookingItem.getItemResource().isWishlisted();
                id = bookingItem.getItemResource().getId();
            }

            if (id != null && !isWishlisted) {
                adapter.removeWishlistedItemFromAdapterById(id);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
