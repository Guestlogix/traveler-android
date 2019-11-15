package com.guestlogix.travelercorekit.models;

import android.util.Log;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class QueryItemListMappingFactory implements ObjectMappingFactory<List<CatalogItem>> {
    @Override
    public List<CatalogItem> instantiate(String rawResponse) throws Exception {
        JSONArray jsonArray = new JSONArray(rawResponse);

        List<CatalogItem> queryCatalogItems = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObjectGLX jsonObject = new JSONObjectGLX(jsonArray.getJSONObject(i).toString());

            QueryType type = QueryType.fromString(jsonObject.getString("type"));
            SearchQuery searchQuery = null;
            switch (type) {
                case BOOKING:
                    BookingItemSearchParameters bookingItemSearchParameters =
                            new BookingItemSearchParameters.BookingItemSearchParametersObjectMappingFactory()
                                    .instantiate(jsonObject.getJSONObject("searchParams").toString());
                    searchQuery = new BookingItemQuery(bookingItemSearchParameters);
                    break;
                case PARKING:
                    ParkingItemSearchParameters parkingItemSearchParameters =
                            new ParkingItemSearchParameters.ParkingItemSearchParametersObjectMappingFactory()
                                    .instantiate(jsonObject.getJSONObject("searchParams").toString());
                    searchQuery = new ParkingItemQuery(parkingItemSearchParameters);
                    break;
                default:
                    Log.i("CatalogItemFactory", "unknown catalog item skipped: " + jsonObject.getString("type"));
                    // if you don't recognize the type skip to the next item
                    continue;
            }

            String title = jsonObject.getNullableString("title");
            String subTitle = jsonObject.getNullableString("subTitle");

            URL thumbnail = null;
            if (!jsonObject.isNull("thumbnail"))
                thumbnail = new URL(jsonObject.getString("thumbnail"));


            queryCatalogItems.add(new QueryItem(title, subTitle, thumbnail, type, searchQuery));
        }

        return queryCatalogItems;

    }
}