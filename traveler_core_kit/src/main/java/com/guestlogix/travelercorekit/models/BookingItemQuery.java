package com.guestlogix.travelercorekit.models;

import java.util.ArrayList;
import java.util.List;

public class BookingItemQuery implements SearchQuery {
    public static final int DEFAULT_PAGE_SIZE = 10;
    private String queryText;
    private PriceRangeFilter priceRangeFilter;
    private List<ProductItemCategory> categories;
    private BoundingBox boundingBox;
    private int offset;
    private int limit;

    public BookingItemQuery(
            String queryText,
            PriceRangeFilter priceRangeFilter,
            List<ProductItemCategory> categories,
            BoundingBox boundingBox,
            int offset,
            int limit) {
        this.queryText = queryText;
        this.priceRangeFilter = priceRangeFilter;
        this.categories = categories;
        this.boundingBox = boundingBox;
        this.offset = offset;
        this.limit = limit;
    }

    public BookingItemQuery(
            String queryText,
            PriceRangeFilter priceRangeFilter,
            BoundingBox boundingBox) {
        this.queryText = queryText;
        this.priceRangeFilter = priceRangeFilter;
        this.categories = new ArrayList<>();
        this.boundingBox = boundingBox;
        this.offset = 0;
        this.limit = DEFAULT_PAGE_SIZE;
    }

    public BookingItemQuery(BookingItemSearchParameters parameters) {
        this.queryText = parameters.getSearchText();
        this.priceRangeFilter = parameters.getPriceRangeFilter();
        this.categories = parameters.getCategories();
        this.boundingBox = parameters.getBoundingBox();
        this.offset = 0;
        this.limit = DEFAULT_PAGE_SIZE;
    }

    public String getQueryText() {
        return queryText;
    }

    public PriceRangeFilter getPriceRangeFilter() {
        return priceRangeFilter;
    }

    public List<ProductItemCategory> getCategories() {
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
