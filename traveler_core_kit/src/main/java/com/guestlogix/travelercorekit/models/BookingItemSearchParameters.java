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
    private List<ProductItemCategory> categories;
    private BoundingBox boundingBox;

    private BookingItemSearchParameters(
            String searchText,
            PriceRangeFilter priceRangeFilter,
            List<ProductItemCategory> categories,
            BoundingBox boundingBox) {
        this.searchText = searchText;
        this.priceRangeFilter = priceRangeFilter;
        this.categories = categories;
        this.boundingBox = boundingBox;
    }


    public String getSearchText() {
        return searchText;
    }

    public PriceRangeFilter getPriceRangeFilter() {
        return priceRangeFilter;
    }

    public List<ProductItemCategory> getCategories() {
        return categories;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    static class BookingItemSearchParametersObjectMappingFactory implements ObjectMappingFactory<BookingItemSearchParameters> {
        @Override
        public BookingItemSearchParameters instantiate(JsonReader reader) throws Exception {
            String searchText = null;
            Double minPrice = null;
            Double maxPrice = null;
            Currency currency = null;
            PriceRangeFilter priceRangeFilter = null;
            List<ProductItemCategory> categories = null;
            Double topLeftLatitude = null;
            Double topLeftLongitude = null;
            Double bottomRightLatitude = null;
            Double bottomRightLongitude = null;
            BoundingBox boundingBox = null;

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

            if (currency != null) {
                Range<Double> range = new Range<>(minPrice, maxPrice);
                priceRangeFilter = new PriceRangeFilter(range, currency);
            }

            if (topLeftLatitude != null && topLeftLongitude != null &&
                    bottomRightLatitude != null && bottomRightLongitude != null){
                Coordinate topLeftCoordinate = new Coordinate(topLeftLatitude, topLeftLongitude);
                Coordinate bottomRightCoordinate = new Coordinate(bottomRightLatitude, bottomRightLongitude);
                boundingBox = new BoundingBox(topLeftCoordinate, bottomRightCoordinate);
            }

            return new BookingItemSearchParameters(searchText, priceRangeFilter, categories, boundingBox);
        }
    }

}
