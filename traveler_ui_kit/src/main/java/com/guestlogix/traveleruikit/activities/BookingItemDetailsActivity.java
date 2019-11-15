package com.guestlogix.traveleruikit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.BookingItemDetailsCallback;
import com.guestlogix.travelercorekit.callbacks.WishlistItemChangedCallback;
import com.guestlogix.travelercorekit.models.BookingItem;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.ActionStripContainerFragment;
import com.guestlogix.traveleruikit.fragments.BookingItemDetailsFragment;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

public class BookingItemDetailsActivity extends AppCompatActivity implements
        BookingItemDetailsCallback, RetryFragment.InteractionListener, WishlistItemChangedCallback {

    public static final String ARG_PRODUCT = "bookingItem";

    private BookingItem bookingItem;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking_item_details);

        this.bookingItem = (BookingItem) getIntent().getSerializableExtra(ARG_PRODUCT);

        if (bookingItem == null) {
            Log.e(this.getLocalClassName(), "No Product");
            finish();
            return;
        }

        setTitle(bookingItem.getTitle());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        reloadBookingItemDetails();
    }

    @Override
    public void onRetry() {
        reloadBookingItemDetails();
    }

    private void reloadBookingItemDetails() {
        Fragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.booking_item_details_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

        Traveler.fetchProductDetails(bookingItem.getItemResource(), this);
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
        transaction.replace(R.id.booking_item_details_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onBookingItemDetailsSuccess(CatalogItemDetails details) {
        BookingItemDetailsFragment fragment = BookingItemDetailsFragment.newInstance(bookingItem, details);
        fragment.setWishlistItemChangedCallback(this);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.booking_item_details_container, fragment);
        transactionQueue.addTransaction(transaction);

        // ActionStrip

        ActionStripContainerFragment stripContainerFragment = ActionStripContainerFragment.newInstance(bookingItem.getItemResource());
        FragmentTransaction stripTransaction = transactionQueue.newTransaction();
        transaction.replace(R.id.actionStripContainerFrameLayout, stripContainerFragment);
        transactionQueue.addTransaction(stripTransaction);
    }

    @Override
    public void onItemWishlistStateChanged(CatalogItemDetails catalogItemDetails) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ARG_PRODUCT, catalogItemDetails);
        setResult(RESULT_OK, resultIntent);
    }
}
