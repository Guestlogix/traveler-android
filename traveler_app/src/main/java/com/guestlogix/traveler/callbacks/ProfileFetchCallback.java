package com.guestlogix.traveler.callbacks;

import com.guestlogix.traveler.models.Profile;

public interface ProfileFetchCallback {
    void onProfileFetchSuccess(Profile profile);
    void onProfileFetchError(Error error);
}
