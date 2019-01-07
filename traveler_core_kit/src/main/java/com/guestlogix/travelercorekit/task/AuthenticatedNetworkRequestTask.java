package com.guestlogix.travelercorekit.task;

import android.util.Log;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.network.AuthenticatedRequest;
import com.guestlogix.travelercorekit.network.Router;
import com.guestlogix.travelercorekit.utilities.InputStreamHelper;

import java.io.IOException;
import java.io.InputStream;

public class AuthenticatedNetworkRequestTask<T> extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private Session mSession;
    private AuthenticatedRequest mRequest;
    protected NetworkTask.ResponseHandler mResponseHandler;

    public AuthenticatedNetworkRequestTask(Session mSession, AuthenticatedRequest mRequest) {
        this.mSession = mSession;
        this.mRequest = mRequest;

        mResponseHandler = new NetworkTask.ResponseHandler() {
            @Override
            public void onHandleResponse(InputStream stream) throws IOException {
                InputStreamHelper.getStringFromInputStream(stream);
            }
        };
    }

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
                //verify network response for error
            }
        };

        BlockTask authTokenFetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                Log.v("Traveler", "Setting Session token:" + authTokenFetchTask.getAuthToken().getValue());
                //TODO: decouple headers from NetworkTask.Request and make it NetworkTask property to update it only in Network Task
                //TODO: rewrite header new token
                mSession.setAuthToken(authTokenFetchTask.getAuthToken());
                mRequest.setToken(mSession.getAuthToken().getValue());
                retryNetworkTask.setRequest(mRequest);
            }
        };

        NetworkTask networkTask = new NetworkTask(mRequest, mResponseHandler);

        BlockTask networkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                //verify network response for error

                if (networkTask.getError() == null) {
                    authTokenFetchTask.cancel();
                    authTokenFetchBlockTask.cancel();
                    retryNetworkTask.cancel();
                    retryNetworkBlockTask.cancel();
                }
            }
        };

        networkBlockTask.addDependency(networkTask);
        authTokenFetchTask.addDependency(networkBlockTask);
        authTokenFetchBlockTask.addDependency(authTokenFetchTask);
        retryNetworkTask.addDependency(authTokenFetchBlockTask);
        retryNetworkBlockTask.addDependency(retryNetworkTask);

        mTaskManager.addTask(networkTask);
        mTaskManager.addTask(networkBlockTask);
        mTaskManager.addTask(authTokenFetchTask);
        mTaskManager.addTask(authTokenFetchBlockTask);
        mTaskManager.addTask(retryNetworkTask);
        mTaskManager.addTask(retryNetworkBlockTask);

        finish();
    }
}
