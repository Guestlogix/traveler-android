package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.travelercorekit.models.BookingContext;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.travelercorekit.utilities.TravelerLog;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.CatalogItemDetailsFragmentDirections;
import com.guestlogix.traveleruikit.fragments.TravelerErrorFragment;
import com.guestlogix.traveleruikit.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.*;

public class CatalogItemDetailsActivity extends AppCompatActivity implements TravelerErrorFragment.OnErrorInteractionListener {

    public static final String ARG_CATALOG_ITEM = "catalog_item";

    private CatalogItemDetailsViewModel catalogItemDetailsViewModel;
    private NavController navController;
    private CatalogItem catalogItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_item_details);

        Bundle extras = getIntent().getExtras();

        if (null != extras && extras.containsKey(ARG_CATALOG_ITEM)) {
            navController = Navigation.findNavController(this, R.id.catalogItemDetailsHostFragment);

            catalogItemDetailsViewModel = ViewModelProviders.of(this).get(CatalogItemDetailsViewModel.class);
            catalogItemDetailsViewModel.getStatus().observe(this, this::onStateChange);
            catalogItemDetailsViewModel.getObservableBookingContext().observe(this, this::onBookingRequest);

            catalogItem = (CatalogItem) extras.getSerializable(ARG_CATALOG_ITEM);
            catalogItemDetailsViewModel.setCatalogItem(catalogItem);
        } else {
            TravelerLog.e(getString(R.string.no_argument_exception), ARG_CATALOG_ITEM, this.getLocalClassName());
            finish();
        }
    }

    private void onStateChange(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                navController.navigate(R.id.loading_action);
                break;
            case SUCCESS:
                navController.navigate(R.id.catalog_item_details_action);
                break;
            case ERROR:
                Bundle arguments = new Bundle();
                arguments.putString(ARG_ERROR_TITLE, getString(R.string.label_sorry));
                arguments.putString(ARG_ERROR_MESSAGE, getString(R.string.label_nothing_to_show));
                arguments.putString(ARG_ERROR_ACTION, getString(R.string.try_again));

                navController.navigate(R.id.error_action, arguments);
                break;
        }
    }

    private void onBookingRequest(BookingContext bookingContext) {
        CatalogItemDetailsFragmentDirections.bookNowAction(bookingContext);
        navController.navigate(CatalogItemDetailsFragmentDirections.bookNowAction(bookingContext));
    }

    @Override
    public void onRetry() {
        catalogItemDetailsViewModel.setCatalogItem(catalogItem);
    }
}
