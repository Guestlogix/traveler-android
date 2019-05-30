package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.OrdersFragment;
import com.guestlogix.traveleruikit.fragments.TravelerFragmentRetryFragment;
import com.guestlogix.traveleruikit.fragments.TravelerLoadingFragment;
import com.guestlogix.traveleruikit.fragments.TravelerRetryFragment;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.*;

public class OrdersActivity extends AppCompatActivity implements TravelerRetryFragment.RetryFragmentInteractionListener {

    public static String ARG_ORDER_QUERY = "arg_order_query";
    private OrderQuery orderQuery;
    private OrderResult orderResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Bundle extras = getIntent().getExtras();
        if (null == extras || !extras.containsKey(ARG_ORDER_QUERY)) {
            TravelerLog.e("ARG_ORDER_QUERY is required to launch OrdersActivity");
            finish();
            return;
        }
        orderQuery = (OrderQuery) extras.getSerializable(ARG_ORDER_QUERY);
        fetchOrders();
    }

    private void fetchOrders() {
        onOrderResultsChange(LOADING);
        Traveler.fetchOrders(orderQuery, 0, fetchOrdersCallback);
    }

    @Override
    public void onRetry() {
        fetchOrders();
    }

    private void onOrderResultsChange(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                TravelerLoadingFragment loadingFragment = new TravelerLoadingFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.ordersContainer, loadingFragment);

                transaction.commit();

                break;
            case SUCCESS:
                OrdersFragment ordersFragment = OrdersFragment.newInstance(orderResult);

                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.ordersContainer, ordersFragment);

                transaction.commit();

                break;
            case ERROR:
                TravelerFragmentRetryFragment errorFragment = TravelerFragmentRetryFragment.getInstance(getString(com.guestlogix.traveleruikit.R.string.label_sorry),
                        getString(com.guestlogix.traveleruikit.R.string.label_nothing_to_show),
                        getString(com.guestlogix.traveleruikit.R.string.try_again));

                errorFragment.setOnInteractionListener(this);

                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.ordersContainer, errorFragment);

                transaction.commit();
                break;
        }
    }

    FetchOrdersCallback fetchOrdersCallback = new FetchOrdersCallback() {
        @Override
        public void onOrdersReceived(OrderResult result, int identifier) {
        }

        @Override
        public void onOrdersFetchError(Error error, int identifier) {
            onOrderResultsChange(ERROR);
        }

        @Override
        public void onOrdersFetchSuccess(OrderResult result, int identifier) {
            orderResult = result;
            onOrderResultsChange(SUCCESS);
        }

        @Override
        public OrderResult getPreviousResult() {
            return null;
        }
    };
}
