package com.guestlogix.task;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.guestlogix.travelercorekit.models.AuthToken;

public class AuthTokenFetchTask extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private Context mContext;
    private String mApiKey;
    private AuthToken mToken;

    public AuthTokenFetchTask(String apiKey, Context context) {
        this.mContext = context;
        this.mApiKey = apiKey;
        this.mToken = new AuthToken("");
    }

    public AuthToken getToken() {
        return mToken;
    }

    @Override
    public void execute() {
        Log.v("Traveler", "AuthTokenFetchTask execute()");

        //Writing encrypted token to shared prefs
        SharedPrefsWriteTask sharedPrefsWriteTask = new SharedPrefsWriteTask(mContext, mApiKey);

        //Encrypting token
        //KeystoreEncryptTask keystoreEncryptTask = new KeystoreEncryptTask(mApiKey);

        //Fetch token from backend
        NetworkTask fetchTokenNetworkTask = new NetworkTask(Router.authenticate(mApiKey, mContext), new JsonObjectMapper<>(new AuthToken.AuthTokenMappingFactory(), new JsonObjectMapper.Callback<AuthToken>() {
            @Override
            public void onSuccess(AuthToken token) {
                Log.v("Traveler", "Fetched  Token:" + token.getValue());
                //keystoreEncryptTask.setData(token.getValue().getBytes());
                sharedPrefsWriteTask.setData(token.getValue());
                mToken = token;
            }

            @Override
            public void onError(Error error) {
                Log.v("Traveler", error.getMessage());
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
