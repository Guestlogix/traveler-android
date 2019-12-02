package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.util.JsonToken.NULL;

public class BookingItemSearchParameters implements Serializable {
    private String searchText;
    private PriceRangeFilter priceRangeFilter;
    private List<BookingItemCategory> categories;
    private BoundingBox boundingBox;
    private BookingItemSort bookingItemSort;
    private String city;

    private BookingItemSearchParameters(
            String searchText,
            PriceRangeFilter priceRangeFilter,
            List<BookingItemCategory> categories,
            BoundingBox boundingBox,
            String city,
            BookingItemSort bookingItemSort) {
        this.searchText = searchText;
        this.priceRangeFilter = priceRangeFilter;
        this.categories = categories;
        this.boundingBox = boundingBox;
        this.city = city;
        this.bookingItemSort = bookingItemSort;
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
        public BookingItemSearchParameters instantiate(JsonReader reader) throws Exception {
            String searchText = null;
            Double minPrice = null;
            Double maxPrice = null;
            Currency currency = null;
            PriceRangeFilter priceRangeFilter = null;
            List<BookingItemCategory> categories = null;
            Double topLeftLatitude = null;
            Double topLeftLongitude = null;
            Double bottomRightLatitude = null;
            Double bottomRightLongitude = null;
            BoundingBox boundingBox = null;
            String city = null;
            BookingItemSortOrder bookingItemSortOrder = null;
            BookingItemSortField bookingItemSortField = null;
            BookingItemSort bookingItemSort = null;
            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "text":
                        searchText = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "topLeftLatitude":
                        topLeftLatitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "topLeftLongitude":
                        topLeftLongitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "bottomRightLatitude":
                        bottomRightLatitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "bottomRightLongitude":
                        bottomRightLongitude = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "minPrice":
                        minPrice = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "maxPrice":
                        maxPrice = JsonReaderHelper.nextNullableDouble(reader);
                        break;
                    case "city":
                        city = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "sortField":
                        String rawSortField = JsonReaderHelper.nextNullableString(reader);
                        if (rawSortField != null) {
                            bookingItemSortField = BookingItemSortField.fromString(rawSortField);
                        }
                        break;
                    case "sortOrder":
                        String rawSortOrder = JsonReaderHelper.nextNullableString(reader);
                        if (rawSortOrder != null) {
                            bookingItemSortOrder = BookingItemSortOrder.fromString(rawSortOrder);
                        }
                        break;
                    case "currency":
                        String code = JsonReaderHelper.nextNullableString(reader);
                        if (code != null) {
                            currency = Currency.getInstance(code);
                        }
                        break;
                    case "categories":
                        if (reader.peek() == NULL) {
                            categories = new ArrayList<>();
                            reader.skipValue();
                        } else {
                            categories = JsonReaderHelper.readCatalogItemCategoryArray(reader);
                        }
                        break;
                    default:
                        reader.skipValue();
                        break;

                }
            }

            reader.endObject();

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

            return new BookingItemSearchParameters(searchText, priceRangeFilter, categories, boundingBox, city, bookingItemSort);
        }
    }

}
