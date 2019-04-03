package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Pass;

import java.util.List;

public interface FetchPassesCallback {
    void onPassFetchSuccess(List<Pass> pass);

    void onPassFetchError(Error error);
}
