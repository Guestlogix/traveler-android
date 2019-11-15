package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.models.BookingProduct;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.AvailabilityFragment;

public class AvailabilityActivity extends AppCompatActivity {
    public static String ARG_PRODUCT = "ARG_PRODUCT";
    public static String TAG = "AvailabilityFragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_availability);

        BookingProduct product = (BookingProduct) getIntent().getSerializableExtra(ARG_PRODUCT);
        if (product == null) {
            Log.e(TAG, "No Product");
            finish();
            return;
        }

        AvailabilityFragment fragment = AvailabilityFragment.getInstance(product);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_availability, fragment);
        transaction.commit();
    }
}
