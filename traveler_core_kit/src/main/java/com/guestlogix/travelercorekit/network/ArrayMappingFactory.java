package com.guestlogix.travelercorekit.network;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayMappingFactory<T> implements ObjectMappingFactory<List<T>> {
    ObjectMappingFactory<T> mObjectMappingFactory;

    public ArrayMappingFactory(ObjectMappingFactory<T> objectMappingFactory) {
        this.mObjectMappingFactory = objectMappingFactory;
    }

    @Override
    public List<T> instantiate(JsonReader reader) throws ObjectMappingException, IOException {
        List<T> objects = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            T model = mObjectMappingFactory.instantiate(reader);
            objects.add(model);
        }

        reader.endArray();
        return objects;
    }
}