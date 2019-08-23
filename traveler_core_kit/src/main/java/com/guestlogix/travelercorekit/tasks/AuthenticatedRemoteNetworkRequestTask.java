package com.guestlogix.travelercorekit.tasks;

import com.guestlogix.travelercorekit.utilities.JsonObjectMapperCallback;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.router.AuthenticatedUrlRequest;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapper;
import com.guestlogix.travelercorekit.utilities.Task;
import com.guestlogix.travelercorekit.utilities.TaskManager;

public class AuthenticatedRemoteNetworkRequestTask<T> extends Task {

    private TaskManager taskManager = new TaskManager();
    private Session session;
    private AuthenticatedUrlRequest request;
    private NetworkTask.ResponseHandler responseHandler;
    private Error error;
    private T resource;

    public AuthenticatedRemoteNetworkRequestTask(Session session, AuthenticatedUrlRequest request, ObjectMappingFactory<T> objectMappingFactory) {
        this.session = session;
        this.request = request;

        if (objectMappingFactory != null) {
            this.responseHandler = new JsonObjectMapper<>(objectMappingFactory, new JsonObjectMapperCallback<T>() {
                @Override
                public void onSuccess(T resource) {
                    AuthenticatedRemoteNetworkRequestTask.this.resource = resource;
                }

                @Override
                public void onError(Error error) {
                    AuthenticatedRemoteNetworkRequestTask.this.error = error;
                }
            });
        } else {
            this.responseHandler = null;
        }
    }

    @Override
    public void execute() {
        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.session.getApiKey(), session.getContext());
        NetworkTask retryNetworkTask = new NetworkTask(request, responseHandler);

        BlockTask retryNetworkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != retryNetworkTask.getError()) {
                    error = retryNetworkTask.getError();
                } else {
                    error = null; // Invalidate previous errors
                }
            }
        };

        BlockTask authTokenFetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null == authTokenFetchTask.getError()) {
                    //TODO: decouple headers from NetworkTask.Request and build it NetworkTask property to update it only in Network Task
                    //TODO: rewrite header new token
                    session.setToken(authTokenFetchTask.getAuthToken());
                    request.setToken(session.getToken().getValue());
                    retryNetworkTask.setRequest(request);
                } else {
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();
                    error = authTokenFetchTask.getError();
                }
            }
        };

        NetworkTask networkTask = new NetworkTask(request, responseHandler);

        BlockTask networkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                NetworkTaskError error = networkTask.getError();

                if (error == null || NetworkTaskError.Code.UNAUTHORIZED != error.getCode()) {
                    authTokenFetchTask.cancel();
                    authTokenFetchBlockTask.cancel();
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();
                }

                if (error != null) {
                    AuthenticatedRemoteNetworkRequestTask.this.error = error;
                }
            }
        };

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                AuthenticatedRemoteNetworkRequestTask.this.finish();
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

    public Error getError() {
        return error;
    }

    public T getResource() {
        return resource;
    }
}
