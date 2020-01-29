package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.PurchasedBookingProduct;

public interface PurchasedBookingProductDetailsFetchCallback {
    /**
     * called when partner offering is fetched successfully
     * @param purchasedBookingProduct is the purchased product details
     */
    void onSuccess(PurchasedBookingProduct purchasedBookingProduct);
    /**
     * called when purchased product details could not be fetched because of a problem
     * @param error is the error details
     */
    void onError(Error error);
}
