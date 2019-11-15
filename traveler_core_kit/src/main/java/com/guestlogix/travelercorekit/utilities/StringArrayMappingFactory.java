package com.guestlogix.travelercorekit.utilities;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class StringArrayMappingFactory implements ObjectMappingFactory<List<String>> {

    @Override
    public List<String> instantiate(String jsonString) throws Exception {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            String item = jsonArray.getString(i);
            resultList.add(item);
        }

        return resultList;
    }
}