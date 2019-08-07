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
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

public class OrdersActivity extends AppCompatActivity implements FetchOrdersCallback, RetryFragment.InteractionListener {
    // TODO: Change naming convention to the following throughout the entire project
    public static String ARG_ORDER_QUERY = "ARG_ORDER_QUERY";

    private OrderQuery query;
    private FragmentTransactionQueue transactionQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orders);

        transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        query = (OrderQuery) getIntent().getSerializableExtra(ARG_ORDER_QUERY);

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
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.ordersContainerFrameLayout, loadingFragment);
        transactionQueue.addTransaction(transaction);

        Traveler.fetchOrders(query, 0, this);
    }

    // FetchOrdersCallback

    // TODO: consider empty cases
    @Override
    public void onOrdersFetchSuccess(OrderResult result, int identifier) {
        // TODO: all statemachines should follow similar pattern as to this file
        Fragment ordersFragment = OrdersFragment.newInstance(result);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.ordersContainerFrameLayout, ordersFragment);
        transactionQueue.addTransaction(transaction);
    }

    @Override
    public void onOrdersFetchError(Error error, int identifier) {
        RetryFragment errorFragment = new RetryFragment();
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.ordersContainerFrameLayout, errorFragment);
        transactionQueue.addTransaction(transaction);
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
