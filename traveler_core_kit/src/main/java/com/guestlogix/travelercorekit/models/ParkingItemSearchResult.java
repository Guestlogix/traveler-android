package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
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

    public void merge(ParkingItemSearchResult searchResult) {
        this.items.addAll(searchResult.getItems());
    }

    static class ParkingItemSearchResultObjectMappingFactory implements ObjectMappingFactory<ParkingItemSearchResult> {
        @Override
        public ParkingItemSearchResult instantiate(JsonReader reader) throws Exception {
            int total = 0;
            List<ParkingItem> parkingItems = null;
            int offset = 0;
            Facets facets = null;
            ParkingItemSearchParameters parameters;
            ParkingItemQuery query = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "total":
                        total = reader.nextInt();
                        break;
                    case "items":
                        parkingItems = new ArrayMappingFactory<>(new ParkingItem.ParkingItemObjectMappingFactory()).instantiate(reader);
                        break;
                    case "offset":
                        offset = reader.nextInt();
                        break;
                    case "aggregation":
                        facets = new Facets.FacetsObjectMappingFactory().instantiate(reader);
                        break;
                    case "parameters":
                        parameters = new ParkingItemSearchParameters.ParkingItemSearchParametersObjectMappingFactory()
                                .instantiate(reader);
                        query = new ParkingItemQuery(parameters);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(parkingItems != null);

            List<ParkingItem> indexedItems = new ArrayList<>();
            for (int i = 0; i < parkingItems.size(); i++) {
                indexedItems.add(i + offset, parkingItems.get(i));
            }

            return new ParkingItemSearchResult(total, parkingItems, facets, query);
        }
    }
}
