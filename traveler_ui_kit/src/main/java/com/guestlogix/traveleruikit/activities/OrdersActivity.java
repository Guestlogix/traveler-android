package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.LoadingFragment;
import com.guestlogix.traveleruikit.fragments.OrdersFragment;
import com.guestlogix.traveleruikit.fragments.RetryFragment;

public class OrdersActivity extends AppCompatActivity implements FetchOrdersCallback, RetryFragment.RetryFragmentInteractionListener {
    // TODO: Change naming convention to the following throughout the entire project
    public static String EXTRA_ORDER_QUERY = "EXTRA_ORDER_QUERY";

    private OrderQuery query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orders);

        query = (OrderQuery) getIntent().getSerializableExtra(EXTRA_ORDER_QUERY);

        if (query == null) {
            Log.e(this.getLocalClassName(), "No OrderQuery in extras");
            finish();
            return;
        }

        reloadOrders();

        // TODO: Remove all navigation stuff (i.e. statemachines that use androidx navigation)
    }

    private void reloadOrders() {
        Fragment loadingFragment = new LoadingFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ordersContainerFrameLayout, loadingFragment);
        transaction.commit();

        Traveler.fetchOrders(query, 0, this);
    }

    // FetchOrdersCallback

    @Override
    public void onOrdersFetchSuccess(OrderResult result, int identifier) {
        // TODO: all statemachines should follow similar pattern as to this file
        Fragment ordersFragment = OrdersFragment.newInstance(result);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ordersContainerFrameLayout, ordersFragment);
        transaction.commit();
    }

    @Override
    public void onOrdersFetchError(Error error, int identifier) {
        RetryFragment errorFragment = new RetryFragment();
        errorFragment.setOnInteractionListener(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ordersContainerFrameLayout, errorFragment);
        transaction.commit();
    }

    @Override
    public void onOrdersFetchReceive(OrderResult result, int identifier) {
        // Do nothing
    }

    @Nullable
    @Override
    public OrderResult getPreviousResult(int identifier) {
        return null;
    }

    // RetryFragmentInteractionListener

    @Override
    public void onRetry() {
        reloadOrders();
    }
}
