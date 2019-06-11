package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.tasks.NetworkTask;

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
        try (JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
            T model = objectMappingFactory.instantiate(reader);

            callback.onSuccess(model);
        } catch (Exception e) {
            // TODO: Fix this
            callback.onError(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, e));
        }
    }
}
