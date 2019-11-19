package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.BookingItemDetailsCallback;
import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.BookingItemDetailsFragment;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

public class ProductDetailsActivity extends AppCompatActivity implements BookingItemDetailsCallback, RetryFragment.InteractionListener {
    public static final String ARG_PRODUCT = "product";

    private Product product;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_details);

        this.product = (Product) getIntent().getSerializableExtra(ARG_PRODUCT);

        if (product == null) {
            Log.e(this.getLocalClassName(), "No Product");
            finish();
            return;
        }

        setTitle(product.getTitle());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        reloadCatalogItemDetails();
    }

    public void onRetry() {
        reloadCatalogItemDetails();
    }

    private void reloadCatalogItemDetails() {
        Fragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.product_details_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

        Traveler.fetchProductDetails(product, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof RetryFragment) {
            ((RetryFragment) fragment).setInteractionListener(this);
        }
    }

    @Override
    public void onBookingItemDetailsError(Error error) {
        Fragment fragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.product_details_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onBookingItemDetailsSuccess(CatalogItemDetails details) {
        BookingItemDetailsFragment fragment = BookingItemDetailsFragment.newInstance((BookingItem) product, details);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.product_details_container, fragment);
        transactionQueue.addTransaction(transaction);
    }
}
