package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

class CatalogGroupListMappingFactory implements ObjectMappingFactory<List<CatalogGroup>> {
    @Override
    public List<CatalogGroup> instantiate(String rawResponse) throws Exception {
        JSONArray jsonArray = new JSONArray(rawResponse);

        List<CatalogGroup> catalogGroups = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObjectGLX jsonObject = new JSONObjectGLX(jsonArray.getJSONObject(i).toString());

            CatalogItemType itemType = CatalogItemType.fromString(jsonObject.getString("type"));
            Assertion.eval(itemType != null);
            List<CatalogItem> items = null;
            switch (itemType) {
                case ITEM:
                    items = new CatalogItemListMappingFactory()
                            .instantiate(jsonObject.getJSONArray("items").toString());
                    break;
                case QUERY:
                    items = new QueryItemListMappingFactory()
                            .instantiate(jsonObject.getJSONArray("items").toString());
                    break;
            }
            String title = jsonObject.getNullableString("title");
            String subTitle = jsonObject.getNullableString("subTitle");
            boolean featured = jsonObject.getBoolean("featured");

            Assertion.eval(items != null);

            catalogGroups.add(new CatalogGroup(title, subTitle, featured, itemType, items));
        }
        return catalogGroups;
    }
}
