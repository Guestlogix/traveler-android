package com.guestlogix.travelercorekit.models;

import java.util.List;

public class BookingQuery implements SearchQuery {
    private String queryText;
    private PriceRange priceRange;
    private List<CatalogItemCategory> categories;
    private BoundingBox boundingBox;
    private int offset;
    private int limit;

    public BookingQuery(
            String queryText,
            PriceRange priceRange,
            List<CatalogItemCategory> categories,
            BoundingBox boundingBox,
            int offset,
            int limit) {
        this.queryText = queryText;
        this.priceRange = priceRange;
        this.categories = categories;
        this.boundingBox = boundingBox;
        this.offset = offset;
        this.limit = limit;
    }

    public BookingQuery(BookingSearchParameters parameters) {
        this.queryText = parameters.getSearchText();
        this.priceRange = parameters.getPriceRange();
        this.categories = parameters.getCategories();
        this.boundingBox = parameters.getBoundingBox();
        this.offset = 0;
        this.limit = 10;
    }

    public String getQueryText() {
        return queryText;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public List<CatalogItemCategory> getCategories() {
        return categories;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int getLimit() {
        return limit;
    }
}
