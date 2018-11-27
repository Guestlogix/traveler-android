package com.guestlogix.travelercorekit.sdk;

import android.content.Context;
import com.guestlogix.TravelerLog;
import com.guestlogix.network.*;
import com.guestlogix.travelercorekit.models.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Traveler {

    private static Traveler mLocalInstance;
    private static Context mContext;
    private static String mApiKey;

    private static AuthToken mToken;

    public static void initialize(@NotNull String apiKey, @Nullable Context applicationContext) {

        if (mLocalInstance != null) {
            TravelerLog.e("SDK already initialized");

        } else {
            mContext = applicationContext;
            mApiKey = apiKey;

            // grab token from Keychain
            // if token doesnt exist in keychain then continue

            NetworkTask authTask = new NetworkTask(Router.authenticate(apiKey, mContext), (JsonObjectMapperCallback) new JsonObjectMapper<AuthToken>(new JsonObjectMapperCallback<AuthToken>() {
                @Override
                public void onSuccess(AuthToken responseObject) {
                    mToken = responseObject;
                }

                @Override
                public void onError(Error error) {
                    // Just log
                }
            }));
        }

    }

}
