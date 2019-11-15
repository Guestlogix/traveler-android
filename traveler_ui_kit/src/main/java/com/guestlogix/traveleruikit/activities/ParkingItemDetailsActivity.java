package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.ParkingItem;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.ActionStripContainerFragment;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.ParkingItemDetailsFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

public class ParkingItemDetailsActivity extends AppCompatActivity implements
        CatalogItemDetailsCallback, RetryFragment.InteractionListener {

    public static final String TAG = "ParkingItemDetailsActiv";
    public static final String ARG_PARKING_ITEM = "ParkingItem";

    private ParkingItem parkingItem;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parking_item_details);

        parkingItem = (ParkingItem) getIntent().getSerializableExtra(ARG_PARKING_ITEM);

        if (parkingItem == null) {
            Log.e(TAG, "No parkingItem");
            finish();
            return;
        }

        setTitle(parkingItem.getTitle());

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        reloadParkingItemDetails();
    }

    @Override
    public void onRetry() {
        reloadParkingItemDetails();
    }

    private void reloadParkingItemDetails() {
        Fragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.parking_item_details_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

        Traveler.fetchProductDetails(parkingItem.getItemResource(), this);
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
    public void onCatalogItemDetailsError(Error error) {
        Fragment fragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.parking_item_details_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onCatalogItemDetailsSuccess(CatalogItemDetails details) {
        String s = details.toString();
        Log.d(TAG, "" + s);
        ParkingItemDetailsFragment fragment = ParkingItemDetailsFragment.newInstance(parkingItem, details);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.parking_item_details_container, fragment);
        transactionQueue.addTransaction(transaction);

        ActionStripContainerFragment stripContainerFragment = ActionStripContainerFragment.newInstance(parkingItem.getItemResource());
        FragmentTransaction stripTransaction = transactionQueue.newTransaction();
        transaction.replace(R.id.actionStripContainerFrameLayout, stripContainerFragment);
        transactionQueue.addTransaction(stripTransaction);
    }
}
