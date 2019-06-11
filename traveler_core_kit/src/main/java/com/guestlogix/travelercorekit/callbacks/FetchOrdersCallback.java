package com.guestlogix.travelercorekit.callbacks;

import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.travelercorekit.models.Pass;

import java.util.List;

public interface FetchOrdersCallback {
    void onOrdersFetchSuccess(OrderResult result, int identifier);
    void onOrdersFetchError(Error error, int identifier);
    @Nullable OrderResult getPreviousResult(int identifier);
    void onOrdersFetchReceive(OrderResult result, int identifier);
}