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
    private BookingItemQuery query;

    private ParkingItemSearchResult(
            int total,
            List<ParkingItem> items,
            Facets facets,
            BookingItemQuery query) {
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

    public BookingItemQuery getQuery() {
        return query;
    }

    static class ParkingItemSearchResultObjectMappingFactory implements ObjectMappingFactory<ParkingItemSearchResult> {
        @Override
        public ParkingItemSearchResult instantiate(JsonReader reader) throws Exception {
            int total = 0;
            List<ParkingItem> catalogItems = null;
            int offset = 0;
            List<ParkingItem> items = null;
            Facets facets = null;
            BookingItemSearchParameters parameters;
            BookingItemQuery query = null;

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "total":
                        total = reader.nextInt();
                        break;
                    case "items":
                        catalogItems = new ArrayMappingFactory<>(new ParkingItem.ParkingItemObjectMappingFactory())
                                .instantiate(reader);
                        break;
                    case "offset":
                        offset = reader.nextInt();
                        break;
                    case "aggregation":
                        facets = new Facets.FacetsObjectMappingFactory().instantiate(reader);
                        break;
                    case "parameters":
                        parameters = new BookingItemSearchParameters.BookingItemSearchParametersObjectMappingFactory()
                                .instantiate(reader);
                        query = new BookingItemQuery(parameters);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(catalogItems != null);

            List<ParkingItem> indexedItems = new ArrayList<>();
            for (int i = 0; i < catalogItems.size(); i++) {
                indexedItems.add(i + offset, catalogItems.get(i));
            }

            return new ParkingItemSearchResult(total, items, facets, query);
        }
    }
}
