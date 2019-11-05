package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.PurchaseForm;

public interface FetchPurchaseFormCallback {
    void onPurchaseFormFetchSuccess(PurchaseForm purchaseForm);
    void onPurchaseFormFetchError(Error error);
}
