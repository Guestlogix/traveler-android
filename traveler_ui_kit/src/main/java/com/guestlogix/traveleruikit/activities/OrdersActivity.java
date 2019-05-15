package com.guestlogix.traveleruikit.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.fragments.OrdersFragment;
import com.guestlogix.traveleruikit.fragments.TravelerFragmentRetryFragment;
import com.guestlogix.traveleruikit.fragments.TravelerLoadingFragment;
import com.guestlogix.traveleruikit.fragments.TravelerRetryFragment;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.ERROR;
import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.SUCCESS;

public class OrdersActivity extends AppCompatActivity implements TravelerRetryFragment.RetryFragmentInteractionListener {

    private Calendar fromCalendar = Calendar.getInstance();
    private Calendar toCalendar = Calendar.getInstance();
    private ArrayList<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        fetchOrders();
    }

    //TODO: Updated when filters are decided.
    private void fetchOrders() {
        fromCalendar.add(Calendar.DAY_OF_YEAR, -105);
        toCalendar.add(Calendar.DAY_OF_YEAR, 105);

        Traveler.fetchOrders(0, 10, fromCalendar.getTime(), toCalendar.getTime(), fetchOrdersCallback);
    }

    @Override
    public void onRetry() {
        Traveler.fetchOrders(0, 10, fromCalendar.getTime(), toCalendar.getTime(), fetchOrdersCallback);
    }


    private void ordersStateChangeHandler(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                TravelerLoadingFragment loadingFragment = new TravelerLoadingFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.ordersContainer, loadingFragment);

                transaction.commit();

                break;
            case SUCCESS:
                OrdersFragment ordersFragment = OrdersFragment.newInstance(orders);

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
        public void onOrdersFetchSuccess(List<Order> orders) {
            if (null == OrdersActivity.this.orders) {
                OrdersActivity.this.orders = new ArrayList<>();
            }
            OrdersActivity.this.orders.clear();
            OrdersActivity.this.orders.addAll(orders);

            ordersStateChangeHandler(SUCCESS);
        }

        @Override
        public void onOrdersFetchError(Error error) {
            ordersStateChangeHandler(ERROR);
        }
    };
}
