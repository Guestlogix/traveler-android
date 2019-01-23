package com.guestlogix.travelercorekit.network;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayMappingFactory<T> implements ObjectMappingFactory<ArrayList<T>> {
    ObjectMappingFactory<T> mObjectMappingFactory;

    public ArrayMappingFactory(ObjectMappingFactory<T> objectMappingFactory) {
        this.mObjectMappingFactory = objectMappingFactory;
    }

    @Override
    public ArrayList<T> instantiate(JsonReader reader) throws ObjectMappingException, IOException {
        ArrayList<T> objects = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            T model = mObjectMappingFactory.instantiate(reader);
            objects.add(model);
        }

        reader.endArray();
        return objects;
    }
}