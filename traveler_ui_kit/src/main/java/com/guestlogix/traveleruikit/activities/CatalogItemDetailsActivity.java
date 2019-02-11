package com.guestlogix.traveleruikit.activities;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.TravelerErrorFragment;
import com.guestlogix.viewmodels.CatalogItemDetailsViewModel;
import com.guestlogix.viewmodels.StatefulViewModel;

import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_ACTION;
import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_MESSAGE;
import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_TITLE;

public class CatalogItemDetailsActivity extends AppCompatActivity implements TravelerErrorFragment.OnErrorInteractionListener {

    public static final String ARG_CATALOG_ITEM = "catalog_item";
    private static final String TAG = "Traveler UI Kit";

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

            catalogItem = (CatalogItem) extras.getSerializable(ARG_CATALOG_ITEM);
            catalogItemDetailsViewModel.setCatalogItem(catalogItem);
        } else {
            Log.e(TAG, String.format(getString(R.string.no_argument_exception), ARG_CATALOG_ITEM, this.getLocalClassName()));
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

    @Override
    public void onRetry() {
        catalogItemDetailsViewModel.setCatalogItem(catalogItem);
    }
}