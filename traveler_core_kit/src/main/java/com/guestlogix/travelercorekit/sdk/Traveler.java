package com.guestlogix.travelercorekit.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.guestlogix.TravelerLog;
import com.guestlogix.task.*;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Traveler {

    private static Traveler mLocalInstance;
    private TaskManager mTaskManager = new TaskManager();

    private Session mSession;

    public static void initialize(@NotNull String apiKey, @Nullable Context applicationContext) {

        if (mLocalInstance != null) {
            TravelerLog.e("SDK already initialized");
        } else {
            mLocalInstance = new Traveler(apiKey, applicationContext);
        }
    }

    private Traveler(String apiKey, Context context) {
        this.mSession = new Session(apiKey, context);

        //read token from disk
        SessionBeginTask sessionBeginTask = new SessionBeginTask(this.mSession);
        //fetch token from backend if disk does not have one
        AuthTokenFetchTask authTokenFetchTask = new AuthTokenFetchTask(this.mSession.getApiKey(), mSession.getContext());

        BlockTask authTokenFetchBlockTask = new BlockTask() {
            @Override
            protected void main() {
                Log.v("Traveler", "Setting Session token:" + authTokenFetchTask.getToken().getValue());

                mSession.setAuthToken(authTokenFetchTask.getToken());
            }
        };

        BlockTask sessionBeginBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (TextUtils.isEmpty(sessionBeginTask.getSession().getAuthToken().getValue())) {
                    Log.v("Traveler", "Could not Find Token on disk, continue fetch token task...");
                } else {
                    Log.v("Traveler", "Found Token on disk: " + sessionBeginTask.getSession().getAuthToken().getValue());
                    Log.v("Traveler", "Cancelling fetch token tasks");

                    authTokenFetchTask.cancel();
                    authTokenFetchBlockTask.cancel();
                }
            }
        };

        sessionBeginBlockTask.addDependency(sessionBeginTask);
        authTokenFetchTask.addDependency(sessionBeginBlockTask);
        authTokenFetchBlockTask.addDependency(authTokenFetchTask);

        mTaskManager.addTask(sessionBeginTask);
        mTaskManager.addTask(sessionBeginBlockTask);
        mTaskManager.addTask(authTokenFetchTask);
        mTaskManager.addTask(authTokenFetchBlockTask);

    }


    //    public static void getCatalog(String id, CatalogResponseHandler catalogResponseHandler) {
//        if (null == mLocalInstance) {
//            catalogResponseHandler.onError(new Error("SDK not initialized, Initialize by calling Traveler.initialize(); "));
//        } else {
//            mLocalInstance.getCatalog(id, new JsonObjectMapper<>(new Catalog.CatalogMappingFactory(), new JsonObjectMapper.Callback<Catalog>() {
//                @Override
//                public void onSuccess(Catalog catalog) {
//                    Log.v("Traveler", "Fetched  Catalog");
//                    catalogResponseHandler.onSuccess(catalog);
//                }
//
//                @Override
//                public void onError(Error error) {
//                    catalogResponseHandler.onError(error);
//                    //In case of authentication error fetch auth token and re run this call
//
//                    if ("Authentication Error".equals(error.getMessage())) {
//
//                        //TODO: Look if we can chain or group tasks
//                        //mTaskManager.addTask();
//
//                    } else {
//
//                        Log.v("Traveler", error.getMessage());
//                        catalogResponseHandler.onError(error);
//                    }
//                }
//            }));
//        }
//    }
//
    private void getCatalog(String id, NetworkTask.ResponseHandler responseHandler) {


        BlockTask getCatalogRequestBlockTask = new BlockTask() {
            @Override
            protected void main() {

            }
        };

        Router.getCatalogue(mSession, id);
        AuthenticatedNetworkRequestTask getCatalogRequestTask = new AuthenticatedNetworkRequestTask();

    }


    public interface CatalogResponseHandler {
        void onSuccess(Catalog catalog);

        void onError(Error e);
    }

}