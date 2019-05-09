package com.guestlogix.traveler.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.viewmodels.OrdersViewModel;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.traveleruikit.fragments.TravelerRetryFragment;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.Calendar;

import static com.guestlogix.traveleruikit.fragments.TravelerRetryFragment.ARG_ERROR_ACTION;
import static com.guestlogix.traveleruikit.fragments.TravelerRetryFragment.ARG_ERROR_MESSAGE;
import static com.guestlogix.traveleruikit.fragments.TravelerRetryFragment.ARG_ERROR_TITLE;

public class OrdersActivity extends AppCompatActivity implements TravelerRetryFragment.RetryFragmentInteractionListener {

    private NavController navController;
    private OrdersViewModel ordersViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        navController = Navigation.findNavController(findViewById(R.id.ordersHostFragment));
        ordersViewModel = ViewModelProviders.of(OrdersActivity.this).get(OrdersViewModel.class);

        ordersViewModel.getStatus().observe(this, this::ordersStateChangeHandler);

        fetchInitialOrders();

    }

    @Override
    public void onRetry() {
        fetchInitialOrders();
    }

    private void fetchInitialOrders() {
        ordersViewModel.fetchOrders(0,10);
    }

    private void ordersStateChangeHandler(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                navController.navigate(R.id.loading_orders_action);
                break;
            case SUCCESS:
                navController.navigate(R.id.list_orders_action);
                break;
            case ERROR:
                Bundle arguments = new Bundle();
                arguments.putString(ARG_ERROR_TITLE, getString(com.guestlogix.traveleruikit.R.string.label_sorry));
                arguments.putString(ARG_ERROR_MESSAGE, getString(com.guestlogix.traveleruikit.R.string.label_nothing_to_show));
                arguments.putString(ARG_ERROR_ACTION, getString(com.guestlogix.traveleruikit.R.string.try_again));

                navController.navigate(R.id.error_orders_action, arguments);
                break;
        }
    }
}
