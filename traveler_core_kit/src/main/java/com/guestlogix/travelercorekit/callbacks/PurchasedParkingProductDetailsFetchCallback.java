package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.PurchasedParkingProduct;

public interface PurchasedParkingProductDetailsFetchCallback {
    /**
     * called when partner offering is fetched successfully
     * @param purchasedParkingProduct is the purchased product details
     */
    void onSuccess(PurchasedParkingProduct purchasedParkingProduct);
    /**
     * called when purchased product details could not be fetched because of a problem
     * @param error is the error details
     */
    void onError(Error error);
}
