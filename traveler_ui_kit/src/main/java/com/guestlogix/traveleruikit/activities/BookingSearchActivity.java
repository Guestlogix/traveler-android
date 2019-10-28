package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.BookingSearchCallback;
import com.guestlogix.travelercorekit.models.BookingItemQuery;
import com.guestlogix.travelercorekit.models.BookingItemSearchResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.BookingSearchResultFragment;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

public class BookingSearchActivity extends AppCompatActivity
        implements BookingSearchCallback, RetryFragment.InteractionListener {

    private FragmentTransactionQueue transactionQueue;
    private String queryText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking_search);

        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                queryText = query;
                searchBookings();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        this.transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());
    }

    private void searchBookings() {
        Fragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.booking_items_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

        BookingItemQuery query = new BookingItemQuery(queryText, null, null);
        Traveler.searchBookingItems(query, this);
    }

    @Override
    public void onBookingSearchSuccess(BookingItemSearchResult searchResult) {
        Fragment fragment = BookingSearchResultFragment.newInstance(searchResult);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.booking_items_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onBookingSearchError(Error error) {
        RetryFragment errorFragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.ordersContainerFrameLayout, errorFragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onRetry() {
        searchBookings();
    }
}
