package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.callbacks.JsonObjectMapperCallback;
import com.guestlogix.travelercorekit.error.TravelerError;
import com.guestlogix.travelercorekit.error.TravelerErrorCode;
import com.guestlogix.travelercorekit.network.MappingFactory;
import com.guestlogix.travelercorekit.task.NetworkTask;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonObjectMapper<T> implements NetworkTask.ResponseHandler {
    private MappingFactory<T> mMappingFactory;
    private JsonObjectMapperCallback<T> mCallback;

    public JsonObjectMapper(MappingFactory<T> mappingFactory, JsonObjectMapperCallback<T> callback) {
        mMappingFactory = mappingFactory;
        mCallback = callback;
    }

    @Override
    public void onHandleResponse(InputStream stream) {
        try (JsonReader reader = new JsonReader(new InputStreamReader(stream))) {
            T model = mMappingFactory.instantiate(reader);

            mCallback.onSuccess(model);
        } catch (IOException e) {
            mCallback.onError(new TravelerError(TravelerErrorCode.PARSING_ERROR, "Invalid JSON stream"));
        } catch (com.guestlogix.travelercorekit.network.MappingException e) {
            mCallback.onError(e.getError());
        }
    }
}
