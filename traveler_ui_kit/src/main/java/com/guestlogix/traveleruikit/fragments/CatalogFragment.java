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
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.travelercorekit.callbacks.CatalogSearchCallback;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;
import com.guestlogix.traveleruikit.widgets.CatalogWidget;

public class CatalogFragment extends Fragment implements CatalogSearchCallback, RetryFragment.InteractionListener {
    private final static String TAG = "CatalogFragment";
    private final static String ARG_CATALOG_QUERY = "arg_catalog_query";

    private FragmentTransactionQueue transactionQueue;

    public static CatalogFragment newInstance(CatalogQuery catalogQuery) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATALOG_QUERY, catalogQuery);
        CatalogFragment fragment = new CatalogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        reloadCatalog();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transactionQueue = new FragmentTransactionQueue(getChildFragmentManager());
    }

    @Override
    public void onResume() {
        super.onResume();

        transactionQueue.setSuspended(false);
    }

    @Override
    public void onPause() {
        super.onPause();

        transactionQueue.setSuspended(true);
    }

    private void reloadCatalog() {
        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_CATALOG_QUERY)) {
            Log.e(TAG, "No CatalogQuery in arguments");
            return;
        }

        LoadingFragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.catalog_container_layout, loadingFragment);

        transactionQueue.addTransaction(transaction);

        CatalogQuery query = (CatalogQuery) args.get(ARG_CATALOG_QUERY);

        Traveler.fetchCatalog(query, this);
    }

    @Override
    public void onCatalogError(Error error) {
        RetryFragment fragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.catalog_container_layout, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onCatalogSuccess(Catalog catalog) {
        CatalogResultFragment fragment = CatalogResultFragment.newInstance(catalog);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.catalog_container_layout, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);

        if (childFragment instanceof RetryFragment) {
            ((RetryFragment) childFragment).setInteractionListener(this);
        }
    }

    @Override
    public void onRetry() {
        reloadCatalog();
    }
}


