package com.guestlogix.traveleruikit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.QueryItem;
import com.guestlogix.travelercorekit.models.QueryType;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.activities.BookingItemDetailsActivity;
import com.guestlogix.traveleruikit.activities.BookingSearchActivity;
import com.guestlogix.traveleruikit.adapters.CatalogSectionAdapter;

public class CatalogResultFragment extends Fragment implements CatalogSectionAdapter.CatalogSectionAdapterCallback {
    public final static String TAG = "CatalogResultFragment";
    public final static String ARG_CATALOG = "ARG_CATALOG";

    private Catalog catalog;

    public static CatalogResultFragment newInstance(Catalog catalog) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATALOG, catalog);
        CatalogResultFragment fragment = new CatalogResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(ARG_CATALOG)) {
            Log.e(TAG, "No Catalog in arguments");
            return;
        }

        catalog = (Catalog) bundle.get(ARG_CATALOG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_result, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_catalog);

        CatalogSectionAdapter catalogSectionAdapter = new CatalogSectionAdapter(getContext());
        catalogSectionAdapter.setCatalog(catalog);
        catalogSectionAdapter.setOnCatalogItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(catalogSectionAdapter);

        return view;
    }

    @Override
    public void onCatalogItemClick(int sectionId, int itemId) {
        CatalogItem item = catalog.getGroups().get(sectionId).getItems().get(itemId);
        if (item instanceof BookingItem) {
            Intent intent = new Intent(this.getContext(), BookingItemDetailsActivity.class);
            intent.putExtra(BookingItemDetailsActivity.ARG_PRODUCT, item);
            startActivity(intent);
        } else if (item instanceof QueryItem && ((QueryItem) item).getType() == QueryType.BOOKING) {
            Intent searchIntent = new Intent(getContext(), BookingSearchActivity.class);
            searchIntent.putExtra(BookingSearchActivity.KEY_ITEM_QUERY, ((QueryItem) item).getSearchQuery());
            startActivity(searchIntent);
        }

    }
}
