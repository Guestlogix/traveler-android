package com.guestlogix.traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.guestlogix.traveler.BuildConfig;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.viewmodels.CatalogViewModel;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class CatalogActivity extends AppCompatActivity {
    private CatalogViewModel catalogViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        catalogViewModel = ViewModelProviders.of(CatalogActivity.this).get(CatalogViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.catalog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.flight_add_action:
                Intent addFlightIntent = new Intent(CatalogActivity.this, AddFlightActivity.class);
                startActivityForResult(addFlightIntent, CatalogViewModel.ADD_FLIGHT_REQUEST_CODE);
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        catalogViewModel.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

