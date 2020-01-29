package com.guestlogix.traveleruikit.purchasedproduct;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.PurchasedBookingProductDetailsFetchCallback;
import com.guestlogix.travelercorekit.models.PurchasedBookingProduct;
import com.guestlogix.travelercorekit.models.PurchasedProductQuery;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

public class PurchasedBookingProductActivity extends AppCompatActivity implements RetryFragment.InteractionListener, PurchasedBookingProductDetailsFetchCallback {

    public static final String ARG_PURCHASED_PRODUCT_QUERY = "purchasedProductQuery";

    private PurchasedProductQuery purchasedProductQuery;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchased_booking_product);

        this.purchasedProductQuery = (PurchasedProductQuery) getIntent().getSerializableExtra(ARG_PURCHASED_PRODUCT_QUERY);

        if (purchasedProductQuery == null) {
            Log.e(this.getLocalClassName(), "a PurchasedProductQuery is required to use this activity");
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        fetchPurchasedProductDetails();
    }

    @Override
    public void onRetry() {
        fetchPurchasedProductDetails();
    }

    private void fetchPurchasedProductDetails() {
        Fragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.purchased_booking_product_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

        Traveler.fetchPurchasedBookingProductDetails(purchasedProductQuery, this);
    }

    @Override
    public void onSuccess(PurchasedBookingProduct purchasedBookingProduct) {
        PurchasedBookingProductFragment fragment = PurchasedBookingProductFragment.newInstance(purchasedBookingProduct);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.purchased_booking_product_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onError(Error error) {
        Fragment fragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.purchased_booking_product_container, fragment);
        transactionQueue.addTransaction(transaction);
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
}
