package com.guestlogix.traveleruikit.purchasedproduct;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.PurchasedParkingProductDetailsFetchCallback;
import com.guestlogix.travelercorekit.models.PurchasedParkingProduct;
import com.guestlogix.travelercorekit.models.PurchasedProductQuery;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

public class PurchasedParkingProductActivity extends AppCompatActivity implements PurchasedParkingProductDetailsFetchCallback, RetryFragment.InteractionListener {

    public static final String TAG = "ParkingItemDetailsActiv";
    public static final String ARG_PURCHASED_PRODUCT_QUERY = "ARG_PURCHASED_PRODUCT_QUERY";

    private PurchasedProductQuery purchasedProductQuery;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchased_parking_product);

        purchasedProductQuery = (PurchasedProductQuery) getIntent().getSerializableExtra(ARG_PURCHASED_PRODUCT_QUERY);

        if (purchasedProductQuery == null) {
            Log.e(TAG, "a PurchasedProductQuery is required to run this activity");
            finish();
            return;
        }

        if(getSupportActionBar() != null) {
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
        transaction.replace(R.id.purchased_parking_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

        Traveler.fetchPurchasedParkingProductDetails(purchasedProductQuery, this);
    }

    @Override
    public void onSuccess(PurchasedParkingProduct purchasedParkingProduct) {
        PurchasedParkingProductFragment fragment = PurchasedParkingProductFragment.newInstance(purchasedParkingProduct);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.purchased_parking_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onError(Error error) {
        Fragment fragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.purchased_parking_container, fragment);
        transactionQueue.addTransaction(transaction);
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof RetryFragment) {
            ((RetryFragment) fragment).setInteractionListener(this);
        }
    }
}