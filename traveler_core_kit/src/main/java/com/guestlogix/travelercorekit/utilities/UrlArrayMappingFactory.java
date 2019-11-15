package com.guestlogix.travelercorekit.utilities;

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlArrayMappingFactory implements ObjectMappingFactory<List<URL>> {

    @Override
    public List<URL> instantiate(String jsonString) throws Exception {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<URL> resultList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            String item = jsonArray.getString(i);
            resultList.add(new URL(item));
        }

        return resultList;
    }
}