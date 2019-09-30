package com.guestlogix.travelercorekit.callbacks;

import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.models.WishlistResult;

public interface WishlistFetchCallback {
    /**
     * Called when the results have successfully been fetched
     *
     * @param result The WishlistResult that was fetched and merged
     * @param identifier  An integer identifying the request that was made. This will be same identifier
     *      you passed to fetch wishlist call.
     */
    void onWishlistFetchSuccess(WishlistResult result, int identifier);

    /**
     * Called when there was an error fetching the results
     *
     * @param error The Error that caused the issue
     * @param identifier An integer identifying the request that was made. This will be same identifier
     *      you passed to fetch wishlist call.
     */
    void onWishlistFetchError(Error error, int identifier);

    /**
     * Called before supplying the fetched results. This is your opportunity to return any previous (paged) results
     *      you had so the SDK can merge them for you. Default implementation of this method returns nil.
     *
     * @return Any previous WishlistResult that corresponds to the same WishlistQuery. null if none was supplied
     */
    @Nullable WishlistResult getPreviousResult();

    /**
     *  Called when the results have been fetched and merged.
     *  This method is called on a background thread and gives you the opportunity to
     *  substitute any buffering or volatile variables you may be holding.
     *  Default implementation of this method is a no-op.
     *
     * @param result The fetched and merged WishlistResult
     * @param identifier An integer identifying the request that was made. This will be same identifier
     *      you passed to fetch wishlist call.
     */
    void onWishlistFetchReceive(WishlistResult result, int identifier);
}
