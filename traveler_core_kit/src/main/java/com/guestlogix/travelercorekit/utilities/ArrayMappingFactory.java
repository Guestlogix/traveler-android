package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayMappingFactory<T> implements ObjectMappingFactory<List<T>> {
    private ObjectMappingFactory<T> objectMappingFactory;

    public ArrayMappingFactory(ObjectMappingFactory<T> objectMappingFactory) {
        this.objectMappingFactory = objectMappingFactory;
    }

    @Override
    public List<T> instantiate(JsonReader reader) throws Exception {
        List<T> objects = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            T model = objectMappingFactory.instantiate(reader);
            objects.add(model);
        }

        reader.endArray();

        return objects;
    }
}