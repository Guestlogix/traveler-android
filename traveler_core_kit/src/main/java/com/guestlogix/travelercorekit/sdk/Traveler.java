package com.guestlogix.travelercorekit.sdk;

import android.content.Context;
import android.util.Log;
import com.guestlogix.TravelerLog;
import com.guestlogix.task.NetworkTask;
import com.guestlogix.task.Task;
import com.guestlogix.task.TaskManager;
import com.guestlogix.travelercorekit.models.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.guestlogix.network.Router.*;

public class Traveler {

    private static Traveler mLocalInstance;
    private Context mContext;
    private String mApiKey;
    private TaskManager mTaskManager = new TaskManager();

    private static AuthToken mToken;

    public static void initialize(@NotNull String apiKey, @Nullable Context applicationContext) {

        if (mLocalInstance != null) {
            TravelerLog.e("SDK already initialized");
        } else {
            mLocalInstance = new Traveler(apiKey, applicationContext);
        }
    }

    private Traveler(String apiKey, Context context) {
        mApiKey = apiKey;
        mContext = context;

        // grab token from Keychain
        // if token doesnt exist in keychain then continue

        NetworkTask.Request request = new NetworkTask.Request() {
            @Override
            public Method getMethod() {
                return Method.POST;
            }

            @Override
            public URL getURL() throws MalformedURLException {

                return new URL(BASE_URL + END_POINT_AUTH_TOKEN);

            }

            @Override
            public HashMap<String, String> getHeaders() {

                HashMap<String, String> headers = new HashMap<>();
                headers.put(X_API_KEY, "testtesttesttesttest");
                headers.put(CONTENT_TYPE, "application/json");

                return headers;
            }

            @Override
            public void onProvidePayload(OutputStream stream) {

                Map<String, String> payload = new HashMap<>();
                payload.put(DEVICE_ID, "android_123");
                payload.put(OS_VERSION, "oreo");
                payload.put(LANGUAGE, "en");
                payload.put(LOCALE, "en_POSIX");
                payload.put(REGION, "US");
                payload.put(APPLICATION_ID, "555");

                try {

                    JSONObject json = new JSONObject(payload);

                    stream.write(json.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        NetworkTask.ResponseHandler responseHandler = stream -> {

            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }

            Log.v("Traveler", total.toString());

        };

        Task networkTask = new NetworkTask(request, responseHandler);

        mTaskManager.addTask(networkTask);

//        NetworkTask authTask = new NetworkTask(Router.authenticate(apiKey, mContext), new JsonObjectMapperCallback<AuthToken>() {
//            @Override
//            public void onSuccess(AuthToken responseObject) {
//                Log.v("Traveler", responseObject.getToken());
//            }
//
//            @Override
//            public void onError(Error error) {
//                Log.v("Traveler", "onError");
//            }
//        }, new AuthToken.AuthTokenMappingFactory());
//
//        //run this network task via network manager
//        NetworkManager.execute(authTask);


    }

}
