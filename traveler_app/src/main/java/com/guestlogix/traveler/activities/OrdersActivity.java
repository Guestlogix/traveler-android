package com.guestlogix.traveler.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.fragments.LoadingFragment;
import com.guestlogix.traveler.fragments.OrdersFragment;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.fragments.TravelerFragmentRetryFragment;
import com.guestlogix.traveleruikit.fragments.TravelerRetryFragment;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.Calendar;
import java.util.TimeZone;

import static com.guestlogix.travelercorekit.models.OrderQuery.DEFAULT_PAGE_SIZE;
import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.*;

public class OrdersActivity extends AppCompatActivity implements TravelerRetryFragment.RetryFragmentInteractionListener {

    private Calendar fromCalendar = Calendar.getInstance();
    private Calendar toCalendar = Calendar.getInstance();

    private OrderQuery orderQuery;
    private OrderResult orderResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        fromCalendar = Calendar.getInstance();
        fromCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        fromCalendar.add(Calendar.DAY_OF_YEAR, -366);
        toCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        fetchOrders(0, DEFAULT_PAGE_SIZE);
    }

    @Override
    public void onRetry() {
        fetchOrders(0, DEFAULT_PAGE_SIZE);
    }

    private void ordersStateChangeHandler(StatefulViewModel.State state) {
        switch (state) {
            case LOADING:
                LoadingFragment loadingFragment = new LoadingFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.ordersContainer, loadingFragment);

                transaction.commit();

                break;
            case SUCCESS:
                OrdersFragment ordersFragment = OrdersFragment.newInstance(orderResult, orderQuery);

                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.ordersContainer, ordersFragment);

                transaction.commit();

                break;
            case ERROR:
                TravelerFragmentRetryFragment errorFragment = TravelerFragmentRetryFragment.getInstance(getString(com.guestlogix.traveleruikit.R.string.label_sorry),
                        getString(com.guestlogix.traveleruikit.R.string.label_nothing_to_show),
                        getString(com.guestlogix.traveleruikit.R.string.try_again));

                errorFragment.setOnInteractionListener(this::onRetry);

                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.ordersContainer, errorFragment);

                transaction.commit();
                break;
        }
    }

    public void fetchOrders(Integer skip, Integer take) {

        ordersStateChangeHandler(LOADING);
        orderQuery = new OrderQuery(skip, take, fromCalendar.getTime(), toCalendar.getTime());

        orderQuery.setSkip(skip);
        orderQuery.setTake(take);
        Traveler.fetchOrders(orderQuery, fetchOrdersCallback);
    }

    private FetchOrdersCallback fetchOrdersCallback = new FetchOrdersCallback() {
        @Override
        public void onOrdersFetchSuccess(OrderResult orders) {
            orderResult = orders;
            ordersStateChangeHandler(SUCCESS);
        }

        @Override
        public void onOrderResultsFetched(OrderResult orders) {

        }

        @Override
        public OrderResult getPreviousOrderResults() {
            return orderResult;
        }

        @Override
        public void onOrdersFetchError(Error error) {
            ordersStateChangeHandler(ERROR);
        }
    };
}
