package com.guestlogix.traveler.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.guestlogix.travelercorekit.callbacks.FetchOrdersCallback;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderResults;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.viewmodels.StatefulViewModel;

import java.util.Calendar;
import java.util.List;

import static com.guestlogix.traveleruikit.viewmodels.StatefulViewModel.State.*;

public class OrdersViewModel extends StatefulViewModel {

    private MutableLiveData<OrderResults> ordersList = new MutableLiveData<>();

    public OrdersViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<OrderResults> getObservableOrdersList() {
        return ordersList;
    }

    //TODO: Updated when filters are decided.
    public void fetchOrders() {
        status.setValue(LOADING);
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.DAY_OF_YEAR, -105);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.add(Calendar.DAY_OF_YEAR, 105);

        Traveler.fetchOrders(0, 10, fromCalendar.getTime(), toCalendar.getTime(), fetchOrdersCallback);
    }

    FetchOrdersCallback fetchOrdersCallback = new FetchOrdersCallback() {
        @Override
        public void onOrdersFetchSuccess(OrderResults orders) {
            status.setValue(SUCCESS);
            ordersList.setValue(orders);
        }

        @Override
        public void onOrdersFetchError(Error error) {
            status.setValue(ERROR);
        }
    };
}
