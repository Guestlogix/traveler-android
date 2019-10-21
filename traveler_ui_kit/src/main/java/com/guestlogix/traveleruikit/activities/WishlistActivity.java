package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.WishlistFragment;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

public class WishlistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        setTitle("Saved List");

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        FragmentTransactionQueue transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());
        Fragment fragment = WishlistFragment.newInstance();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.wishlist_container, fragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
