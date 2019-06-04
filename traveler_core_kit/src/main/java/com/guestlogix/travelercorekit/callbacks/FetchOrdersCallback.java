package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.OrderResult;

public interface FetchOrdersCallback {
    void onOrdersReceived(OrderResult order, int identifier);

    void onOrdersFetchError(Error error, int identifier);

    void onOrdersFetchSuccess(OrderResult orders, int identifier);

    OrderResult getPreviousResult();
}