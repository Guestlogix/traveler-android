package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

class QueryItemListMappingFactory implements ObjectMappingFactory<List<CatalogItem>> {
    @Override
    public List<CatalogItem> instantiate(String rawResponse) throws Exception {
        JSONArray jsonArray = new JSONArray(rawResponse);

        List<CatalogItem> queryCatalogItems = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObjectGLX jsonObject = new JSONObjectGLX(jsonArray.getJSONObject(i).toString());

            QueryItem queryItem = new QueryItem.QueryItemObjectMappingFactory().instantiate(jsonObject.toString());

            if (queryItem != null) {
                queryCatalogItems.add(queryItem);
            }
        }

        return queryCatalogItems;

    }
}