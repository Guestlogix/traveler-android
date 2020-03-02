package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookingItemSearchParameters implements Serializable {
    private String searchText;
    private PriceRangeFilter priceRangeFilter;
    private List<BookingItemCategory> categories;
    private BoundingBox boundingBox;
    private BookingItemSort bookingItemSort;
    private String city;
    private Coordinate location;

    private BookingItemSearchParameters(
            String searchText,
            PriceRangeFilter priceRangeFilter,
            List<BookingItemCategory> categories,
            BoundingBox boundingBox,
            String city,
            BookingItemSort bookingItemSort,
            Coordinate location) {
        this.searchText = searchText;
        this.priceRangeFilter = priceRangeFilter;
        this.categories = categories;
        this.boundingBox = boundingBox;
        this.city = city;
        this.bookingItemSort = bookingItemSort;
        this.location = location;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public String getSearchText() {
        return searchText;
    }

    public PriceRangeFilter getPriceRangeFilter() {
        return priceRangeFilter;
    }

    public List<BookingItemCategory> getCategories() {
        return categories;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public String getCity() {
        return city;
    }

    public BookingItemSort getBookingItemSort() {
        return bookingItemSort;
    }

    static class BookingItemSearchParametersObjectMappingFactory implements ObjectMappingFactory<BookingItemSearchParameters> {
        @Override
        public BookingItemSearchParameters instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String searchText = "";
            if (!jsonObject.isNull("text"))
                searchText = jsonObject.getString("text");

            Double minPrice = jsonObject.getNullableDouble("minPrice");
            Double maxPrice = jsonObject.getNullableDouble("maxPrice");

            Currency currency = null;
            if (!jsonObject.isNull("currency")) {
                currency = Currency.getInstance(jsonObject.getString("currency"));
            }

            List<BookingItemCategory> categories = new ArrayList<>();
            if (!jsonObject.isNull("subCategories")) {
                categories = new ArrayMappingFactory<>(new BookingItemCategory.CategoryObjectMappingFactory()).instantiate(jsonObject.getJSONArray("subCategories").toString());
            }

            //For bounding box coordinates
            Double topLeftLatitude = jsonObject.getNullableDouble("topLeftLatitude");
            Double topLeftLongitude = jsonObject.getNullableDouble("topLeftLongitude");
            Double bottomRightLatitude = jsonObject.getNullableDouble("bottomRightLatitude");
            Double bottomRightLongitude = jsonObject.getNullableDouble("bottomRightLongitude");

            //For radius based coordinates (as of Feb 2020 radius is assumed in Backend so only lat long is received)
            Double latitude = jsonObject.getNullableDouble("latitude");
            Double longitude = jsonObject.getNullableDouble("longitude");


            BoundingBox boundingBox = null;
            PriceRangeFilter priceRangeFilter = null;
            String city = jsonObject.getString("city");

            BookingItemSortOrder bookingItemSortOrder = null;
            String rawSortOrder = jsonObject.getString("sortOrder");
            if (rawSortOrder != null) {
                bookingItemSortOrder = BookingItemSortOrder.fromString(rawSortOrder);
            }

            BookingItemSortField bookingItemSortField = null;
            String rawSortField = jsonObject.getString("sortField");
            if (rawSortField != null) {
                bookingItemSortField = BookingItemSortField.fromString(rawSortField);
            }

            BookingItemSort bookingItemSort = null;

            if (currency != null && maxPrice != null && minPrice != null) {
                Range<Double> range = new Range<>(minPrice, maxPrice);
                priceRangeFilter = new PriceRangeFilter(range, currency);
            }

            if (topLeftLatitude != null && topLeftLongitude != null &&
                    bottomRightLatitude != null && bottomRightLongitude != null) {
                Coordinate topLeftCoordinate = new Coordinate(topLeftLatitude, topLeftLongitude);
                Coordinate bottomRightCoordinate = new Coordinate(bottomRightLatitude, bottomRightLongitude);
                boundingBox = new BoundingBox(topLeftCoordinate, bottomRightCoordinate);
            }

            if (bookingItemSortField != null && bookingItemSortOrder != null) {
                bookingItemSort = new BookingItemSort(bookingItemSortField, bookingItemSortOrder);
            }

            Coordinate location = null;
            if(latitude != null && longitude != null) {
                location = new Coordinate(latitude, longitude);
            }
            return new BookingItemSearchParameters(searchText, priceRangeFilter, categories, boundingBox, city, bookingItemSort, location);
        }
    }

}
