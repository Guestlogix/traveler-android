package com.guestlogix.travelercorekit.models;

import java.util.ArrayList;
import java.util.List;

public class BookingItemQuery implements SearchQuery {
    public static final int DEFAULT_PAGE_SIZE = 10;
    private String queryText;
    private String city;
    private PriceRangeFilter priceRangeFilter;
    private List<BookingItemCategory> categories;
    private BoundingBox boundingBox;
    private BookingItemSort bookingItemSort;
    private int offset;
    private int limit;

    public BookingItemQuery(
            String queryText,
            String city,
            PriceRangeFilter priceRangeFilter,
            List<BookingItemCategory> categories,
            BoundingBox boundingBox,
            BookingItemSort bookingItemSort,
            int offset,
            int limit) {
        this.queryText = queryText;
        this.priceRangeFilter = priceRangeFilter;
        this.categories = categories;
        this.boundingBox = boundingBox;
        this.offset = offset;
        this.limit = limit;
        this.city = city;
        this.bookingItemSort = bookingItemSort;
    }

    public BookingItemQuery(
            String queryText,
            String city,
            List<BookingItemCategory> categories) {
        this.queryText = queryText;
        this.city = city;
        this.categories = new ArrayList<>();
        this.offset = 0;
        this.limit = DEFAULT_PAGE_SIZE;
        this.categories = categories;
    }

    public BookingItemQuery(
            String queryText,
            String city,
            BookingItemSort bookingItemSort,
            PriceRangeFilter priceRangeFilter) {
        this.queryText = queryText;
        this.city = city;
        this.categories = new ArrayList<>();
        this.offset = 0;
        this.limit = DEFAULT_PAGE_SIZE;
        this.bookingItemSort = bookingItemSort;
        this.priceRangeFilter = priceRangeFilter;
    }

    public BookingItemQuery(BookingItemSearchParameters parameters) {
        this.queryText = parameters.getSearchText();
        this.priceRangeFilter = parameters.getPriceRangeFilter();
        this.categories = parameters.getCategories();
        this.boundingBox = parameters.getBoundingBox();
        this.bookingItemSort = parameters.getBookingItemSort();
        this.offset = 0;
        this.limit = DEFAULT_PAGE_SIZE;
        this.city = parameters.getCity();
    }

    public String getCity() {
        return city;
    }

    public String getQueryText() {
        return queryText;
    }

    public PriceRangeFilter getPriceRangeFilter() {
        return priceRangeFilter;
    }

    public List<BookingItemCategory> getCategories() {
        return categories;
    }

    public BookingItemSort getBookingItemSort() {
        return bookingItemSort;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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
