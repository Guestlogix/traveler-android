package com.guestlogix.travelercorekit.task;

import android.content.Context;
import android.util.Log;
import com.guestlogix.travelercorekit.callbacks.JsonObjectMapperCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.AuthToken;
import com.guestlogix.travelercorekit.network.Router;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapper;

public class AuthTokenFetchTask extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private Context mContext;
    private String mApiKey;
    private AuthToken mAuthToken;
    private TravelerError mError;

    public AuthTokenFetchTask(String apiKey, Context context) {
        this.mContext = context;
        this.mApiKey = apiKey;
    }

    public AuthToken getAuthToken() {
        return mAuthToken;
    }

    public TravelerError getError() {
        return mError;
    }

    @Override
    public void execute() {
        Log.v("Traveler", "AuthTokenFetchTask execute()");

        //Writing encrypted token to shared prefs
        SharedPrefsWriteTask sharedPrefsWriteTask = new SharedPrefsWriteTask(mContext, mApiKey);

        //Encrypting token
        //KeystoreEncryptTask keystoreEncryptTask = new KeystoreEncryptTask(mApiKey);

        //Fetch token from backend
        NetworkTask fetchTokenNetworkTask = new NetworkTask(Router.authenticate(mApiKey, mContext), new JsonObjectMapper<>(new AuthToken.AuthTokenMappingFactory(), new JsonObjectMapperCallback<AuthToken>() {
            @Override
            public void onSuccess(AuthToken token) {
                Log.v("Traveler", "Fetched  Token:" + token.getValue());
                //keystoreEncryptTask.setData(token.getValue().getBytes());
                sharedPrefsWriteTask.setData(token.getValue());
                mAuthToken = token;
            }

            @Override
            public void onError(TravelerError error) {
                Log.v("Traveler", error.toString());
                mError = error;
            }
        }));

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                AuthTokenFetchTask.this.finish();
            }
        };

        sharedPrefsWriteTask.addDependency(fetchTokenNetworkTask);

        finishTask.addDependency(sharedPrefsWriteTask);

        mTaskManager.addTask(fetchTokenNetworkTask);
        mTaskManager.addTask(sharedPrefsWriteTask);
        mTaskManager.addTask(finishTask);

    }
}
