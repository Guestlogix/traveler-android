package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderResults;
import com.guestlogix.travelercorekit.models.Pass;

import java.util.List;

public interface FetchOrdersCallback {
    void onOrdersFetchSuccess(OrderResults orders);

    void onOrdersFetchError(Error error);
}
