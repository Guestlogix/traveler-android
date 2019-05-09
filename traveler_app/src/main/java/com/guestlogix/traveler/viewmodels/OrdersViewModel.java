package com.guestlogix.traveler.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.OrderQuery;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.Calendar;

import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.*;

public class OrdersViewModel extends StatefulViewModel {

    private MutableLiveData<OrderResult> ordersResult = new MutableLiveData<>();
    private OrderQuery orderQuery;

    public OrdersViewModel(@NonNull Application application) {
        super(application);
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.DAY_OF_YEAR, -105);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.add(Calendar.DAY_OF_YEAR, 105);

        orderQuery = new OrderQuery(0, 10, fromCalendar.getTime(), toCalendar.getTime());

    }

    public LiveData<OrderResult> getObservableOrdersResult() {
        return ordersResult;
    }

    //TODO: Updated when filters are decided.
    public void fetchOrders(Integer skip, Integer take) {
        if (null == status.getValue())
            status.setValue(LOADING);

        orderQuery.setOffset(skip);
        orderQuery.setLimit(take);
        Traveler.fetchOrders(orderQuery, fetchOrdersCallback);
    }

    private FetchOrdersCallback fetchOrdersCallback = new FetchOrdersCallback() {
        @Override
        public void onOrdersFetchSuccess(OrderResult orders) {
            if (status.getValue() == LOADING)
                status.setValue(SUCCESS);
            ordersResult.postValue(orders);
        }

        @Override
        public void onOrderResultsFetched(OrderResult orders) {

        }

        @Override
        public OrderResult getPreviousOrderResults() {
            return ordersResult.getValue();
        }

        @Override
        public void onOrdersFetchError(Error error) {
            status.setValue(ERROR);
        }
    };
}
