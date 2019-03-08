package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import com.guestlogix.travelercorekit.callbacks.JsonObjectMapperCallback;
import com.guestlogix.travelercorekit.models.ObjectMappingError;
import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.models.Token;
import com.guestlogix.travelercorekit.network.Router;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapper;

public class AuthTokenFetchTask extends Task {

    private TaskManager taskManager = new TaskManager();
    private Context context;
    private String apiKey;
    private Token token;
    private TravelerError error;

    public AuthTokenFetchTask(String apiKey, Context context) {
        this.context = context;
        this.apiKey = apiKey;
    }

    public Token getAuthToken() {
        return token;
    }

    public TravelerError getError() {
        return error;
    }

    @Override
    public void execute() {
        //Writing encrypted token to shared prefs
        SharedPrefsWriteTask sharedPrefsWriteTask = new SharedPrefsWriteTask(context, apiKey);

        //Encrypting token
        //KeystoreEncryptTask keystoreEncryptTask = new KeystoreEncryptTask(apiKey);

        //Fetch token from backend
        NetworkTask fetchTokenNetworkTask = new NetworkTask(Router.authenticate(apiKey, context), new JsonObjectMapper<>(new Token.AuthTokenObjectMappingFactory(), new JsonObjectMapperCallback<Token>() {
            @Override
            public void onSuccess(Token token) {
                //keystoreEncryptTask.setData(token.getValue().getBytes());
                sharedPrefsWriteTask.setData(token.getValue());
                AuthTokenFetchTask.this.token = token;
            }

            @Override
            public void onError(ObjectMappingError error) {
                AuthTokenFetchTask.this.error = new TravelerError(error.getCode(), error.getMessage());
            }
        }));

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                error = fetchTokenNetworkTask.getError();
                AuthTokenFetchTask.this.finish();
            }
        };

        sharedPrefsWriteTask.addDependency(fetchTokenNetworkTask);
        finishTask.addDependency(sharedPrefsWriteTask);

        taskManager.addTask(fetchTokenNetworkTask);
        taskManager.addTask(sharedPrefsWriteTask);
        taskManager.addTask(finishTask);
    }
}
