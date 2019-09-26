package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Product;

public interface WishlistAddCallback {
    /**
     * Called when the {@link Product} was wishlisted successfully
     *
     * @param item The {@link Product} that was wishlisted
     * @param itemDetails The corresponding {@link CatalogItemDetails} of the {@link Product} that was wishlisted
     */
    void onWishlistAddSuccess(Product item, CatalogItemDetails itemDetails);

    /**
     * Called when there was an error wishlisting the {@link Product}
     *
     * @param error The Error representing the reason for failure.
     */
    void onWishlistAddError(Error error);
}
