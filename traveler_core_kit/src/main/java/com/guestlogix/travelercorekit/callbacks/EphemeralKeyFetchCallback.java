package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.EphemeralKey;

public interface EphemeralKeyFetchCallback {
    void onEphemeralKeyFetchSuccess(EphemeralKey key);
    void onEphemeralKeyError(Error error);
}
