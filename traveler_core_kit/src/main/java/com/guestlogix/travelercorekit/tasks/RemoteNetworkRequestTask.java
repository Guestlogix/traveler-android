package com.guestlogix.travelercorekit.tasks;

import com.guestlogix.travelercorekit.UrlRequest;
import com.guestlogix.travelercorekit.utilities.*;

public class RemoteNetworkRequestTask<T> extends Task {

    private TaskManager taskManager = new TaskManager();
    private UrlRequest request;
    private JsonObjectMapper<T> jsonObjectMapper;
    private ObjectMappingFactory<T> objectMappingFactory;
    private Error error;
    private T resource;

    public RemoteNetworkRequestTask(UrlRequest request, ObjectMappingFactory<T> objectMappingFactory) {
        this.request = request;
        this.objectMappingFactory = objectMappingFactory;

        jsonObjectMapper = new JsonObjectMapper<>(this.objectMappingFactory, new JsonObjectMapperCallback<T>() {
            @Override
            public void onSuccess(T resource) {
                RemoteNetworkRequestTask.this.resource = resource;
            }

            @Override
            public void onError(Error error) {
                RemoteNetworkRequestTask.this.error = error;
            }
        });
    }

    @Override
    public void execute() {

        NetworkTask retryNetworkTask = new NetworkTask(request, jsonObjectMapper);

        BlockTask retryNetworkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != retryNetworkTask.getError()) {
                    error = retryNetworkTask.getError();
                }
            }
        };

        NetworkTask networkTask = new NetworkTask(request, jsonObjectMapper);

        BlockTask networkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                NetworkTaskError error = networkTask.getError();

                if (error == null || NetworkTaskError.Code.UNAUTHORIZED != error.getCode()) {
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();
                }

                if (error != null) {
                    RemoteNetworkRequestTask.this.error = error;
                }
            }
        };

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                RemoteNetworkRequestTask.this.finish();
            }
        };
        networkBlockTask.addDependency(networkTask);
        retryNetworkTask.addDependency(networkBlockTask);
        retryNetworkBlockTask.addDependency(retryNetworkTask);
        finishTask.addDependency(retryNetworkBlockTask);

        taskManager.addTask(networkTask);
        taskManager.addTask(networkBlockTask);
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
