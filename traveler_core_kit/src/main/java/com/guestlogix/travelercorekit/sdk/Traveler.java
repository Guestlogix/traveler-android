package com.guestlogix.travelercorekit.sdk;

import android.content.Context;
import android.util.Log;
import com.guestlogix.TravelerLog;
import com.guestlogix.task.*;
import com.guestlogix.travelercorekit.models.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Traveler {

    private static Traveler mLocalInstance;
    private Context mContext;
    private String mApiKey;
    private TaskManager mTaskManager = new TaskManager();

    private static AuthToken mToken;

    public static void initialize(@NotNull String apiKey, @Nullable Context applicationContext) {

        if (mLocalInstance != null) {
            TravelerLog.e("SDK already initialized");

        } else {
            mLocalInstance = new Traveler(apiKey, applicationContext);
        }
    }

    private Traveler(String apiKey, Context context) {
        this.mApiKey = apiKey;
        this.mContext = context;

        // grab token from Keychain
        // if token doesnt exist in keychain then continue

        JsonObjectMapper<AuthToken> jsonObjectMapper = new JsonObjectMapper<>(new AuthToken.AuthTokenMappingFactory(), new JsonObjectMapper.Callback<AuthToken>() {
            @Override
            public void onSuccess(AuthToken token) {
                Log.v("Traveler", "Token:" + token.getToken());
            }

            @Override
            public void onError(Error error) {
                Log.v("Traveler", error.getMessage());
            }
        });

        Task networkTask = new NetworkTask(Router.authenticate(apiKey, context), jsonObjectMapper);

        mTaskManager.addTask(networkTask);

    }

}
