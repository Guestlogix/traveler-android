package com.guestlogix.traveleruikit.itinerary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.models.ItineraryQuery;
import com.guestlogix.traveleruikit.R;

public class ItineraryActivity extends AppCompatActivity {

    private static final String ARG_ITINERARY_QUERY = "ARG_ITINERARY_QUERY";

    public static Intent buildIntent(@NonNull Context context, @NonNull ItineraryQuery query) {
        Intent intent = new Intent(context, ItineraryActivity.class);
        intent.putExtra(ARG_ITINERARY_QUERY, query);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        if (getIntent().getSerializableExtra(ARG_ITINERARY_QUERY) == null) {
            Log.e(this.getLocalClassName(), "Itinerary Query should not be null. Use `ItineraryActivity.buildIntent(context, query)` to create Intent");
            finish();
            return;
        }

        ItineraryQuery itineraryQuery = (ItineraryQuery) getIntent().getSerializableExtra(ARG_ITINERARY_QUERY);
        Fragment fragment = ItineraryPreviewFragment.newInstance(itineraryQuery);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.itinerary_fragment_container, fragment)
                .commit();
    }
}