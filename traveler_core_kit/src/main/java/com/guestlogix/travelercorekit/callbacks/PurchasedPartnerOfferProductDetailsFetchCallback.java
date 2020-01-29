package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.PurchasedPartnerOfferingProduct;

public interface PurchasedPartnerOfferProductDetailsFetchCallback {
    /**
     * called when partner offering is fetched successfully
     * @param purchasedPartnerOfferingProduct is the purchased product details
     */
    void onSuccess(PurchasedPartnerOfferingProduct purchasedPartnerOfferingProduct);
    /**
     * called when purchased product details could not be fetched because of a problem
     * @param error is the error details
     */
    void onError(Error error);
}
