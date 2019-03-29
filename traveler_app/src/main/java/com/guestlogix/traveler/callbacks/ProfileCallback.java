package com.guestlogix.traveler.callbacks;

import com.guestlogix.traveler.models.Profile;

public interface ProfileCallback {
    void onSignInSuccess(Profile profile);
    void onSignInError(Error error);
}
