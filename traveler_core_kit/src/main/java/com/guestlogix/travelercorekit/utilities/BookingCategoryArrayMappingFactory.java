package com.guestlogix.travelercorekit.utilities;

import com.guestlogix.travelercorekit.models.BookingItemCategory;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class BookingCategoryArrayMappingFactory implements ObjectMappingFactory<List<BookingItemCategory>> {

    @Override
    public List<BookingItemCategory> instantiate(String jsonString) throws Exception {
        JSONArray jsonArray = new JSONArray(jsonString);
        List<BookingItemCategory> resultList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            String item = jsonArray.getString(i);
            resultList.add(BookingItemCategory.fromString(item));
        }

        return resultList;
    }
}