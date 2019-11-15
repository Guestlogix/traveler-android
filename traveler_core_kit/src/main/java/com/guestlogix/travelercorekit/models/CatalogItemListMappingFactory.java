package com.guestlogix.travelercorekit.models;

import android.util.Log;

import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CatalogItemListMappingFactory implements ObjectMappingFactory<List<CatalogItem>> {
    @Override
    public List<CatalogItem> instantiate(String rawResponse) throws Exception {
        JSONArray jsonArray = new JSONArray(rawResponse);
        List<CatalogItem> resultList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ProductType productType = ProductType.fromString(jsonObject.getString("purchaseStrategy"));

            switch (productType) {
                case BOOKABLE:
                    resultList.add(new BookingItem.BookingItemObjectMappingFactory().instantiate(jsonObject.toString()));
                    break;
                case PARKING:
                    resultList.add(new ParkingItem.ParkingItemObjectMappingFactory().instantiate(jsonObject.toString()));
                    break;
                case PARTNER_OFFERING:
                    resultList.add(new PartnerOfferingItem.PartnerOfferingItemObjectMappingFactory().instantiate(jsonObject.toString()));
                    break;
                default:
                    Log.i("CatalogItemFactory", "unknown catalog item skipped: " + jsonObject.getString("purchaseStrategy"));
                    break;
            }
        }

        return resultList;
    }
}
