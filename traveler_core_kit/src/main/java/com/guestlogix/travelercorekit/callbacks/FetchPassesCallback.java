package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.Pass;

import java.util.List;

public interface FetchPassesCallback {
    void onSuccess(List<Pass> pass);
    void onError(Error error);
}
