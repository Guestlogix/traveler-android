package com.guestlogix.traveler.callbacks;

import com.guestlogix.traveler.models.Profile;

public interface ProfileCallback {
    void onProfileReceived(Profile profile);
    void onProfileError(Error error);
}
