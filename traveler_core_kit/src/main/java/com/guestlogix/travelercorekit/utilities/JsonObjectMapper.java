package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.Log;

import com.guestlogix.travelercorekit.BuildConfig;
import com.guestlogix.travelercorekit.tasks.NetworkTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        if (BuildConfig.DEBUG) {
            stream = logRawJson(stream);
        }

        try (JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
            T model = objectMappingFactory.instantiate(reader);

            callback.onSuccess(model);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(new ObjectMappingError(objectMappingFactory, e));
        }
    }

    private InputStream logRawJson(InputStream inputStream) {

        String responseBodyString;

        byte[] is = new byte[0];
        try {
            is = readFully(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        responseBodyString = convertStreamToString(new ByteArrayInputStream(is));


        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("NetworkTask",
                        " \n RESPONSE BODY:" + ((responseBodyString.trim().isEmpty()) ? " [NO RESPONSE BODY]" : "\n" + responseBodyString) +
                        "\n------------------------\n \n");

        return new ByteArrayInputStream(is);
    }

    String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
}
