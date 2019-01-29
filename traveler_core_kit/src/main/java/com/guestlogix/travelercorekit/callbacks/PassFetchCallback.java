package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Pass;
import java.util.List;

public interface PassFetchCallback {

    void onPassFetchSuccess(List<Pass> pass);
    void onPassFetchError(TravelerError error);
}
