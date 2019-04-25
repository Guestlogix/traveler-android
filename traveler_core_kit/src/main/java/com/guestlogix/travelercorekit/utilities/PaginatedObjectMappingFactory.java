package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaginatedObjectMappingFactory<T> implements ObjectMappingFactory<List<T>> {
    private ArrayMappingFactory<T> arrayMappingFactory;

    public PaginatedObjectMappingFactory(ArrayMappingFactory<T> objectMappingFactory) {
        this.arrayMappingFactory = objectMappingFactory;
    }

    @Override
    public List<T> instantiate(JsonReader reader) throws ObjectMappingException, IOException {
        String key;
        List<T> objects = new ArrayList<>();

        JsonToken token = reader.peek();
        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        }
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