package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayMappingFactory<T> implements ObjectMappingFactory<List<T>> {
    private ObjectMappingFactory<T> mObjectMappingFactory;

    public ArrayMappingFactory(ObjectMappingFactory<T> objectMappingFactory) {
        this.mObjectMappingFactory = objectMappingFactory;
    }

    @Override
    public List<T> instantiate(JsonReader reader) throws ObjectMappingException, IOException {
        List<T> objects = new ArrayList<>();

        JsonToken token = reader.peek();
        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        }
        reader.beginArray();
        while (reader.hasNext()) {
            T model = mObjectMappingFactory.instantiate(reader);
            objects.add(model);
        }

        reader.endArray();
        return objects;
    }
}