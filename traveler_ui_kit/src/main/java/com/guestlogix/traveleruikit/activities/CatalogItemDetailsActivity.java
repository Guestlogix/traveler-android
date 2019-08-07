package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.travelercorekit.callbacks.CatalogItemDetailsCallback;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.CatalogItemDetailsFragment;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

public class CatalogItemDetailsActivity extends AppCompatActivity implements CatalogItemDetailsCallback, RetryFragment.InteractionListener {

    public static final String ARG_PRODUCT = "product";

    private Product product;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_catalog_item_details);

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
        transaction.replace(R.id.catalog_item_details_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

        Traveler.fetchCatalogItemDetails(product, this);
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
        transaction.replace(R.id.catalog_item_details_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onCatalogItemDetailsSuccess(CatalogItemDetails details) {
        Fragment fragment = CatalogItemDetailsFragment.newInstance(details);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.catalog_item_details_container, fragment);
        transactionQueue.addTransaction(transaction);
    }
}
