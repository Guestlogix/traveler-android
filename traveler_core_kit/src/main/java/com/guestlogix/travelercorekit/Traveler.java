package com.guestlogix.travelercorekit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.guestlogix.travelercorekit.callbacks.FlightSearchCallback;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.travelercorekit.models.FlightQuery;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.network.AuthenticatedRequest;
import com.guestlogix.travelercorekit.network.MappingFactory;
import com.guestlogix.travelercorekit.network.Router;
import com.guestlogix.travelercorekit.task.*;
import com.guestlogix.travelercorekit.utilities.JsonObjectMapper;
import com.guestlogix.travelercorekit.utilities.TravelerLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
                //Log.v("Traveler", "Setting Session token:" + authTokenFetchTask.getAuthToken().getValue());

                mSession.setAuthToken(authTokenFetchTask.getAuthToken());
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


    public static void flightSearch(FlightQuery query, FlightSearchCallback flightSearchCallback) {

        if (null == mLocalInstance) {
            flightSearchCallback.onFlightSearchError(new Error("SDK not initialized, Initialize by calling Traveler.initialize();"));
        } else {
            AuthenticatedRequest request = Router.searchFlight(mLocalInstance.mSession, query);

            AuthenticatedNetworkRequestTask searchFlightTask = new AuthenticatedNetworkRequestTask(mLocalInstance.mSession, request, new JsonObjectMapper<>((reader -> {
                List<Flight> flights = new ArrayList<>();
                Flight.FlightMappingFactory flightMappingFactory = new Flight.FlightMappingFactory();

                reader.beginArray();

                while (reader.hasNext()) {
                    flights.add(flightMappingFactory.instantiate(reader));
                }
                reader.endArray();

                return flights;
            }), new JsonObjectMapper.Callback<List<Flight>>() {

                @Override
                public void onSuccess(List<Flight> model) {
                    // TODO: handle success.
                }

                @Override
                public void onError(Error error) {
                    // TODO: handle failure.
                }
            }));

            BlockTask searchFlightBlockTask = new BlockTask() {
                @Override
                protected void main() {

                }
            };

            searchFlightBlockTask.addDependency(searchFlightTask);

            mLocalInstance.mTaskManager.addTask(searchFlightTask);
            mLocalInstance.mTaskManager.addTask(searchFlightBlockTask);

        }
    }


}