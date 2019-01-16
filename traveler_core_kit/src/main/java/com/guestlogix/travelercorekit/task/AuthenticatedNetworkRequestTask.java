package com.guestlogix.travelercorekit.task;

import android.util.Log;
import com.guestlogix.travelercorekit.callbacks.JsonObjectMapperCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.network.AuthenticatedRequest;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapper;

public class AuthenticatedNetworkRequestTask<T> extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private Session mSession;
    private AuthenticatedRequest mRequest;
    private JsonObjectMapper<T> mJsonObjectMapper;
    private ObjectMappingFactory<T> mObjectMappingFactory;
    private TravelerError mError;
    private T mResource;

    public AuthenticatedNetworkRequestTask(Session session, AuthenticatedRequest request, ObjectMappingFactory<T> objectMappingFactory) {
        this.mSession = session;
        this.mRequest = request;
        this.mObjectMappingFactory = objectMappingFactory;

        mJsonObjectMapper = new JsonObjectMapper<>(mObjectMappingFactory, new JsonObjectMapperCallback<T>() {
            @Override
            public void onSuccess(T resource) {
                Log.v("Traveler", "AuthenticatedNetworkRequestTask: onSuccess()");
                mResource = resource;
            }

            @Override
            public void onError(TravelerError error) {
                Log.v("Traveler", error.toString());
                mError = error;
            }
        });
    }

    @Override
    public void execute() {

        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.mSession.getApiKey(), mSession.getContext());

        NetworkTask retryNetworkTask = new NetworkTask(mRequest, mJsonObjectMapper);

        BlockTask retryNetworkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != retryNetworkTask.getError()) {
                    mError = retryNetworkTask.getError();
                }
            }
        };

        BlockTask authTokenFetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null == authTokenFetchTask.getError()) {
                    Log.v("Traveler", "Setting Session token:" + authTokenFetchTask.getAuthToken().getValue());
                    //TODO: decouple headers from NetworkTask.Request and make it NetworkTask property to update it only in Network Task
                    //TODO: rewrite header new token
                    mSession.setAuthToken(authTokenFetchTask.getAuthToken());
                    mRequest.setToken(mSession.getAuthToken().getValue());
                    retryNetworkTask.setRequest(mRequest);
                } else {
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();
                    mError = authTokenFetchTask.getError();
                }
            }
        };

        NetworkTask networkTask = new NetworkTask(mRequest, mJsonObjectMapper);

        BlockTask networkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                TravelerError error = networkTask.getError();
                if (error == null || TravelerErrorCode.UNAUTHORIZED != error.getCode()) {
                    authTokenFetchTask.cancel();
                    authTokenFetchBlockTask.cancel();
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();

                    //mResource = networkTask.
                    mError = error;
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


        mTaskManager.addTask(networkTask);
        mTaskManager.addTask(networkBlockTask);
        mTaskManager.addTask(authTokenFetchTask);
        mTaskManager.addTask(authTokenFetchBlockTask);
        mTaskManager.addTask(retryNetworkTask);
        mTaskManager.addTask(retryNetworkBlockTask);
        mTaskManager.addTask(finishTask);

        //finish();
    }

    public TravelerError getError() {
        return mError;
    }

    public T getResource() {
        return mResource;
    }
}
