package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.BookingItemCategory;

import java.util.List;

public interface BookingItemCategoriesSearchCallback {
    void onCategoriesSearchSuccess(List<BookingItemCategory> categories);
    void onCategoriesSearchError(Error error);
}
