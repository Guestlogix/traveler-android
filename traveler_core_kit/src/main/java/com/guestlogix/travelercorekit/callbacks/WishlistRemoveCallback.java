package com.guestlogix.travelercorekit.callbacks;

import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.models.CatalogItemDetails;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.WishlistResult;

public interface WishlistRemoveCallback {
    /**
     * @param item The {@link Product} that was unwishlisted
     * @param itemDetails  The corresponding {@link CatalogItemDetails} of the {@link Product} that was removed from the wishlist.
     *                   If the removed {@link Product} is unavailable, it will be nil because there's no corresponding
     *                   {@link CatalogItemDetails} of a unavailable {@link Product}.
     */
    void onWishlistRemoveSuccess(Product item, CatalogItemDetails itemDetails);

    /**
     * @param error The `Error` representing the reason for failure
     * @param result The WishlistResult used to initiate the removal operation
     */
    void onWishlistRemoveError(Error error, @Nullable WishlistResult result);
}
