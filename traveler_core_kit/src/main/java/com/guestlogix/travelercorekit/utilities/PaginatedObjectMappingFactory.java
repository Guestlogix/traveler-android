package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import java.util.ArrayList;
import java.util.List;

public class PaginatedObjectMappingFactory<T> implements ObjectMappingFactory<List<T>> {
    private ArrayMappingFactory<T> arrayMappingFactory;

    public PaginatedObjectMappingFactory(ArrayMappingFactory<T> objectMappingFactory) {
        this.arrayMappingFactory = objectMappingFactory;
    }

    @Override
    public List<T> instantiate(JsonReader reader) throws Exception {
        String key;
        List<T> objects = new ArrayList<>();

        reader.beginObject();

        while (reader.hasNext()) {
            key = reader.nextName();
            switch (key) {
                case "result":
                    objects = arrayMappingFactory.instantiate(reader);
                    break;
                default:
                    reader.skipValue();
            }
        }

        reader.endObject();

        return objects;
    }
}