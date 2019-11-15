package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookingItemSearchResult implements Serializable {
    private int total;
    private List<BookingItem> items;
    private Facets facets;
    private BookingItemQuery query;

    private BookingItemSearchResult(
            int total,
            List<BookingItem> items,
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

    public List<BookingItem> getItems() {
        return items;
    }

    public Facets getFacets() {
        return facets;
    }

    public BookingItemQuery getQuery() {
        return query;
    }

    public void merge(BookingItemSearchResult searchResult) {
        this.items.addAll(searchResult.getItems());
    }

    static class BookingItemSearchResultObjectMappingFactory implements ObjectMappingFactory<BookingItemSearchResult> {
        @Override
        public BookingItemSearchResult instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            int total = jsonObject.getInt("total");
            List<BookingItem> catalogItems = new ArrayMappingFactory<>(new BookingItem.BookingItemObjectMappingFactory())
                    .instantiate(jsonObject.getJSONArray("items").toString());
            int offset = 0;
            if (!jsonObject.isNull("offset"))
                offset = jsonObject.getInt("offset");
            List<BookingItem> items;
            Facets facets = new Facets.FacetsObjectMappingFactory().instantiate(jsonObject.getJSONObject("aggregation").toString());
            BookingItemQuery query = new BookingItemQuery(new BookingItemSearchParameters.BookingItemSearchParametersObjectMappingFactory()
                    .instantiate(jsonObject.getJSONObject("parameters").toString()));


            Assertion.eval(catalogItems != null);

            List<BookingItem> indexedItems = new ArrayList<>();
            for (int i = 0; i < catalogItems.size(); i++) {
                indexedItems.add(i + offset, catalogItems.get(i));
            }

            items = catalogItems;

            return new BookingItemSearchResult(total, items, facets, query);
        }
    }
}
