package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapperCallback;
import com.guestlogix.travelercorekit.models.Token;
import com.guestlogix.travelercorekit.Router;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapper;
import com.guestlogix.travelercorekit.utilities.Task;
import com.guestlogix.travelercorekit.utilities.TaskManager;

public class AuthTokenFetchTask extends Task {

    private TaskManager taskManager = new TaskManager();
    private Token token;
    private String apiKey;
    private Context context;
    private Error error;

    public AuthTokenFetchTask(String apiKey, Context context) {
        this.apiKey = apiKey;
        this.context = context;
    }

    public Token getAuthToken() {
        return token;
    }

    public Error getError() {
        return error;
    }

    @Override
    public void execute() {
        // Writing encrypted token to shared prefs
        SharedPrefsWriteTask sharedPrefsWriteTask = new SharedPrefsWriteTask(context, apiKey);
        NetworkTask.Route route = Router.authenticate(apiKey, context);

        // Fetch token from backend
        NetworkTask fetchTokenNetworkTask = new NetworkTask(route, new JsonObjectMapper<>(new Token.AuthTokenObjectMappingFactory(), new JsonObjectMapperCallback<Token>() {
            @Override
            public void onSuccess(Token token) {
                sharedPrefsWriteTask.setData(token.getValue());
                AuthTokenFetchTask.this.token = token;
            }

            @Override
            public void onError(Error error) {
                AuthTokenFetchTask.this.error = error;
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
