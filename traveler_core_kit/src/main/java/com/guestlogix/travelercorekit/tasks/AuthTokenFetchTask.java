package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import com.guestlogix.travelercorekit.callbacks.JsonObjectMapperCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.models.Token;
import com.guestlogix.travelercorekit.network.Router;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapper;

public class AuthTokenFetchTask extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private Context mContext;
    private String mApiKey;
    private Token mToken;
    private TravelerError mError;

    public AuthTokenFetchTask(String apiKey, Context context) {
        this.mContext = context;
        this.mApiKey = apiKey;
    }

    public Token getAuthToken() {
        return mToken;
    }

    public TravelerError getError() {
        return mError;
    }

    @Override
    public void execute() {
        //Writing encrypted token to shared prefs
        SharedPrefsWriteTask sharedPrefsWriteTask = new SharedPrefsWriteTask(mContext, mApiKey);

        //Encrypting token
        //KeystoreEncryptTask keystoreEncryptTask = new KeystoreEncryptTask(mApiKey);

        //Fetch token from backend
        NetworkTask fetchTokenNetworkTask = new NetworkTask(Router.authenticate(mApiKey, mContext), new JsonObjectMapper<>(new Token.AuthTokenObjectMappingFactory(), new JsonObjectMapperCallback<Token>() {
            @Override
            public void onSuccess(Token token) {
                //keystoreEncryptTask.setData(token.getValue().getBytes());
                sharedPrefsWriteTask.setData(token.getValue());
                mToken = token;
            }

            @Override
            public void onError(TravelerError error) {
                mError = error;
            }
        }));

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                mError = fetchTokenNetworkTask.getError();
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
