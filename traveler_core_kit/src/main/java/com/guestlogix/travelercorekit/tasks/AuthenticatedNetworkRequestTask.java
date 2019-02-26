package com.guestlogix.travelercorekit.tasks;

import com.guestlogix.travelercorekit.callbacks.JsonObjectMapperCallback;
import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.models.TravelerErrorCode;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.network.AuthenticatedRequest;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapper;

public class AuthenticatedNetworkRequestTask<T> extends Task {

    private TaskManager taskManager = new TaskManager();
    private Session session;
    private AuthenticatedRequest request;
    private JsonObjectMapper<T> jsonObjectMapper;
    private ObjectMappingFactory<T> objectMappingFactory;
    private TravelerError error;
    private T resource;

    public AuthenticatedNetworkRequestTask(Session session, AuthenticatedRequest request, ObjectMappingFactory<T> objectMappingFactory) {
        this.session = session;
        this.request = request;
        this.objectMappingFactory = objectMappingFactory;

        jsonObjectMapper = new JsonObjectMapper<>(this.objectMappingFactory, new JsonObjectMapperCallback<T>() {
            @Override
            public void onSuccess(T resource) {
                AuthenticatedNetworkRequestTask.this.resource = resource;
            }

            @Override
            public void onError(TravelerError error) {
                AuthenticatedNetworkRequestTask.this.error = error;
            }
        });
    }

    @Override
    public void execute() {
        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.session.getApiKey(), session.getContext());
        NetworkTask retryNetworkTask = new NetworkTask(request, jsonObjectMapper);

        BlockTask retryNetworkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != retryNetworkTask.getError()) {
                    error = retryNetworkTask.getError();
                }
            }
        };

        BlockTask authTokenFetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null == authTokenFetchTask.getError()) {
                    //TODO: decouple headers from NetworkTask.Request and make it NetworkTask property to update it only in Network Task
                    //TODO: rewrite header new token
                    session.setAuthToken(authTokenFetchTask.getAuthToken());
                    request.setToken(session.getAuthToken().getValue());
                    retryNetworkTask.setRequest(request);
                } else {
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();
                    error = authTokenFetchTask.getError();
                }
            }
        };

        NetworkTask networkTask = new NetworkTask(request, jsonObjectMapper);

        BlockTask networkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                TravelerError error = networkTask.getError();

                if (error == null || TravelerErrorCode.UNAUTHORIZED != error.getCode()) {
                    authTokenFetchTask.cancel();
                    authTokenFetchBlockTask.cancel();
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();
                }

                if (error != null) {
                    AuthenticatedNetworkRequestTask.this.error = error;
                }
            }
        };

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                AuthenticatedNetworkRequestTask.this.finish();
            }
        };
        networkBlockTask.addDependency(networkTask);
        authTokenFetchTask.addDependency(networkBlockTask);
        authTokenFetchBlockTask.addDependency(authTokenFetchTask);
        retryNetworkTask.addDependency(authTokenFetchBlockTask);
        retryNetworkBlockTask.addDependency(retryNetworkTask);
        finishTask.addDependency(retryNetworkBlockTask);

        taskManager.addTask(networkTask);
        taskManager.addTask(networkBlockTask);
        taskManager.addTask(authTokenFetchTask);
        taskManager.addTask(authTokenFetchBlockTask);
        taskManager.addTask(retryNetworkTask);
        taskManager.addTask(retryNetworkBlockTask);
        taskManager.addTask(finishTask);
    }

    public TravelerError getError() {
        return error;
    }

    public T getResource() {
        return resource;
    }
}
