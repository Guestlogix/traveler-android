package com.guestlogix.travelercorekit.utilities;

import android.util.Log;

import com.guestlogix.travelercorekit.BuildConfig;
import com.guestlogix.travelercorekit.tasks.NetworkTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonObjectMapper<T> implements NetworkTask.ResponseHandler {
    private ObjectMappingFactory<T> objectMappingFactory;
    private JsonObjectMapperCallback<T> callback;

    public JsonObjectMapper(ObjectMappingFactory<T> objectMappingFactory, JsonObjectMapperCallback<T> callback) {
        this.objectMappingFactory = objectMappingFactory;
        this.callback = callback;
    }

    @Override
    public void onHandleResponse(InputStream stream) {
        try {

            long startMilis = System.currentTimeMillis();
            String rawResponse = convertStreamToString(stream);
            long endMilis = System.currentTimeMillis();

            if (BuildConfig.DEBUG) {

                logRawResponse(rawResponse);

                Log.d("Statistics", "converting stream into json took " + (endMilis - startMilis) + " mili seconds");
            }

            startMilis = System.currentTimeMillis();
            T model = objectMappingFactory.instantiate(rawResponse);
            endMilis = System.currentTimeMillis();

            if (BuildConfig.DEBUG) {
                Log.d("Statistics", "converting json into object took " + (endMilis - startMilis) + " mili seconds");
            }

            callback.onSuccess(model);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(new ObjectMappingError(objectMappingFactory, e));
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    private void logRawResponse(String rawResponse) {
        if (rawResponse.trim().isEmpty()) {
            Log.d("NetworkTask", " \n RESPONSE BODY: [NO RESPONSE BODY] \n------------------------\n \n");
        } else {
            StringBuilder logMessage = new StringBuilder();
            logMessage.append(" \n RESPONSE BODY:\n");
            int charPerLine = 1000;
            int loggedLinePerResponse = 4;
            String shortenedResponse;

            if (rawResponse.length() > charPerLine * loggedLinePerResponse)
                shortenedResponse = rawResponse.substring(0, charPerLine * loggedLinePerResponse) + "....\n....";
            else
                shortenedResponse = rawResponse;

            for (int i = 0; i < shortenedResponse.length(); i += charPerLine) {
                logMessage.append(shortenedResponse.substring(i, Math.min(shortenedResponse.length(), i + charPerLine)));
                logMessage.append("\n");
            }
            Log.d("NetworkTask", logMessage.toString() + "\n------------------------\n \n");

        }
    }

    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String inputLine;
        StringBuilder responseStringBuilder = new StringBuilder();
        while (true) {
            inputLine = in.readLine();
            if (inputLine == null) break;
            responseStringBuilder.append(inputLine);
        }

        return responseStringBuilder.toString();
    }
}
