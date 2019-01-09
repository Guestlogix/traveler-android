package com.guestlogix.travelercorekit.task;

import android.util.Log;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.network.AuthenticatedRequest;

public class AuthenticatedNetworkRequestTask<T> extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private Session mSession;
    private AuthenticatedRequest mRequest;
    protected NetworkTask.ResponseHandler mResponseHandler;
    private TravelerError mError;

    public AuthenticatedNetworkRequestTask(Session mSession, AuthenticatedRequest mRequest, NetworkTask.ResponseHandler mResponseHandler) {
        this.mSession = mSession;
        this.mRequest = mRequest;
        this.mResponseHandler = mResponseHandler;
    }

    @Override
    public void execute() {

        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.mSession.getApiKey(), mSession.getContext());

        NetworkTask retryNetworkTask = new NetworkTask(mRequest, mResponseHandler);

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

        NetworkTask networkTask = new NetworkTask(mRequest, mResponseHandler);

        BlockTask networkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                TravelerError error = networkTask.getError();
                if (error == null || TravelerErrorCode.UNAUTHORIZED != error.getCode()) {
                    authTokenFetchTask.cancel();
                    authTokenFetchBlockTask.cancel();
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();

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
}
