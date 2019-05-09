package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.OrderResult;

public interface FetchOrdersCallback {
    void onOrdersFetchSuccess(OrderResult orders);

    void onOrderResultsFetched(OrderResult orders);

    OrderResult getPreviousOrderResults();

    void onOrdersFetchError(Error error);
}
