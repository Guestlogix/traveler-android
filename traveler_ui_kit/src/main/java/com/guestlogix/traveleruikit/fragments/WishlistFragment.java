package com.guestlogix.traveleruikit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.WishlistResult;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.CatalogItemDetailsActivity;
import com.guestlogix.traveleruikit.adapters.WishlistAdapter;

public class WishlistFragment extends Fragment implements WishlistAdapter.OnItemClickListener{
    public static final int PAGE_SIZE = 10;
    private static final String TAG = "WishlistFragment";
    private static final String ARG_WISHLIST_RESULT = "ARG_WISHLIST_RESULT";

    public WishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrdersFragment.
     */
    public static WishlistFragment newInstance() {
        return new WishlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_ordersFragment_orders);
        WishlistAdapter adapter = new WishlistAdapter(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onWishlistedItemClick(CatalogItem catalogItem) {
        Intent intent = new Intent(this.getContext(), CatalogItemDetailsActivity.class);
        intent.putExtra(CatalogItemDetailsActivity.ARG_PRODUCT, catalogItem);
        startActivity(intent);
    }
}
