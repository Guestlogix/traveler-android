package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.CatalogItemDetails;

public interface WishlistItemChangedCallback {
    /**
     * Called when an {@Link CatalogItemDetails}'s isFavorite state changes
     * @param catalogItemDetails the item whose stated has been updated
     */
    void onItemWishlistStateChanged(CatalogItemDetails catalogItemDetails);
}
