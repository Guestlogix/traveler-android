package com.guestlogix.traveler_stripe_payment_provider;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.callbacks.EphemeralKeyFetchCallback;
import com.guestlogix.travelercorekit.models.EphemeralKey;
import com.guestlogix.travelercorekit.models.Traveler;
import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;

public class TravelerEphemeralKeyProvider implements EphemeralKeyProvider {
    @Override
    public void createEphemeralKey(@NonNull String apiVersion, @NonNull EphemeralKeyUpdateListener keyUpdateListener) {
        Traveler.fetchEphemeralStripeCustomerKey(apiVersion, new EphemeralKeyFetchCallback() {
            @Override
            public void onEphemeralKeyFetchSuccess(EphemeralKey key) {
                keyUpdateListener.onKeyUpdate(key.getKey());
            }

            @Override
            public void onEphemeralKeyError(Error error) {
                // TODO: Pass responseCode and message from Error when casted as a NetworkError

                keyUpdateListener.onKeyUpdateFailure(400, "Error");
            }
        });
    }
}
