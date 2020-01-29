package com.guestlogix.travelercorekit.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArrayMappingFactory<T> implements ObjectMappingFactory<List<T>> {
    private ObjectMappingFactory<T> objectMappingFactory;

    public ArrayMappingFactory(ObjectMappingFactory<T> objectMappingFactory) {
        this.objectMappingFactory = objectMappingFactory;
    }

    @Override
    public ArrayList<T> instantiate(String jsonString) throws Exception {
        JSONArray jsonArray = new JSONArray(jsonString);
        ArrayList<T> resultList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            resultList.add(objectMappingFactory.instantiate(item.toString()));
        }

        return resultList;
    }
}