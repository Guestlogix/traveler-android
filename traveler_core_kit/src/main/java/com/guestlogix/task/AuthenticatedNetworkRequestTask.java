package com.guestlogix.task;

import android.util.Log;
import com.guestlogix.travelercorekit.models.Session;

public class AuthenticatedNetworkRequestTask<T> extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private Session mSession;
    private NetworkTask.Request mRequest;
    private NetworkTask.ResponseHandler mResponseHandler;


    @Override
    public void execute() {

        BlockTask verifyTokenBlockTask = new BlockTask() {
            @Override
            protected void main() {

            }
        };

        NetworkTask networkTask = new NetworkTask(mRequest, mResponseHandler);

        BlockTask networkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                //verify network response for error
            }
        };

        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.mSession.getApiKey(), mSession.getContext());

        BlockTask authTokenFetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                Log.v("Traveler", "Setting Session token:" + authTokenFetchTask.getToken().getValue());

                mSession.setAuthToken(authTokenFetchTask.getToken());
                //TODO: rewrite header new token
            }
        };

        NetworkTask retryNetworkTask = new NetworkTask(mRequest, mResponseHandler);

        BlockTask retryNetworkBlockTask = new BlockTask() {
            @Override
            protected void main() {
                //verify network response for error
            }
        };

        networkTask.addDependency(verifyTokenBlockTask);
        networkBlockTask.addDependency(networkTask);
        authTokenFetchTask.addDependency(networkBlockTask);
        authTokenFetchBlockTask.addDependency(authTokenFetchBlockTask);
        retryNetworkTask.addDependency(authTokenFetchBlockTask);
        retryNetworkBlockTask.addDependency(retryNetworkTask);


        mTaskManager.addTask(verifyTokenBlockTask);
        mTaskManager.addTask(networkTask);
        mTaskManager.addTask(networkBlockTask);
        mTaskManager.addTask(authTokenFetchTask);
        mTaskManager.addTask(authTokenFetchBlockTask);
        mTaskManager.addTask(retryNetworkTask);
        mTaskManager.addTask(retryNetworkBlockTask);

        finish();
    }
}
