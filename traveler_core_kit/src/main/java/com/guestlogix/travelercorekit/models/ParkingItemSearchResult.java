package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.List;

public class ParkingItemSearchResult implements Serializable {
    private int total;
    private List<ParkingItem> items;
    private Facets facets;
    private ParkingItemQuery query;

    private ParkingItemSearchResult(
            int total,
            List<ParkingItem> items,
            Facets facets,
            ParkingItemQuery query) {
        this.total = total;
        this.items = items;
        this.facets = facets;
        this.query = query;
    }

    public int getTotal() {
        return total;
    }

    public List<ParkingItem> getItems() {
        return items;
    }

    public Facets getFacets() {
        return facets;
    }

    public ParkingItemQuery getQuery() {
        return query;
    }

    static class ParkingItemSearchResultObjectMappingFactory implements ObjectMappingFactory<ParkingItemSearchResult> {
        @Override
        public ParkingItemSearchResult instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            int total = jsonObject.getInt("total");
            List<ParkingItem> catalogItems = new ArrayMappingFactory<>(new ParkingItem.ParkingItemObjectMappingFactory())
                    .instantiate(jsonObject.getJSONArray("items").toString());
            int offset = jsonObject.getInt("total");
            Facets facets = new Facets.FacetsObjectMappingFactory().instantiate(jsonObject.getJSONObject("aggregation").toString());
            ParkingItemQuery query = new ParkingItemQuery(new ParkingItemSearchParameters.ParkingItemSearchParametersObjectMappingFactory()
                    .instantiate(jsonObject.getJSONObject("parameters").toString()));


            Assertion.eval(catalogItems != null);

            return new ParkingItemSearchResult(total, catalogItems, facets, query);
        }
    }
}
