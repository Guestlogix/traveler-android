package com.guestlogix.travelercorekit.models;


import android.util.Log;

import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class PurchasedProductListMappingFactory implements ObjectMappingFactory<List<Product>> {

    @Override
    public List<Product> instantiate(String rawResponse) throws Exception {
        JSONArray jsonArray = new JSONArray(rawResponse);
        List<Product> resultList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ProductType productType = ProductType.fromString(jsonObject.getString("purchaseStrategy"));

            switch (productType) {
                case BOOKABLE:
                    resultList.add(new PurchasedBookingProduct.BookingPurchasedProductObjectMappingFactory().instantiate(jsonObject.toString()));
                    break;
                case PARKING:
                    resultList.add(new PurchasedParkingProduct.ParkingPurchasedProductObjectMappingFactory().instantiate(jsonObject.toString()));
                    break;
                case PARTNER_OFFERING:
                    resultList.add(new PurchasedPartnerOfferingProduct.PartnerOfferingPurchasedProductObjectMappingFactory().instantiate(jsonObject.toString()));
                    break;
                default:
                    Log.i("PurchasedProductFactory", "unknown product skipped: " + jsonObject.getString("purchaseStrategy"));
                    break;
            }
        }
        return resultList;
    }
}
