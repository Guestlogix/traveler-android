package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.models.TravelerError;
import com.guestlogix.travelercorekit.models.TravelerErrorCode;
import com.guestlogix.travelercorekit.tasks.NetworkTask;

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
        try (JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
            T model = objectMappingFactory.instantiate(reader);

            callback.onSuccess(model);
        } catch (IOException e) {
            callback.onError(new TravelerError(TravelerErrorCode.PARSING_ERROR, "Invalid JSON stream"));
        } catch (ObjectMappingException e) {
            callback.onError(e.getError());
        }
    }
}
