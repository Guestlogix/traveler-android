package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.util.JsonToken.NULL;

public class BookingSearchParameters implements Serializable {
    private String searchText;
    private PriceRange priceRange;
    private List<CatalogItemCategory> categories;
    private BoundingBox boundingBox;

    private BookingSearchParameters(
            String searchText,
            PriceRange priceRange,
            List<CatalogItemCategory> categories,
            BoundingBox boundingBox) {
        this.searchText = searchText;
        this.priceRange = priceRange;
        this.categories = categories;
        this.boundingBox = boundingBox;
    }


    public String getSearchText() {
        return searchText;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public List<CatalogItemCategory> getCategories() {
        return categories;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    static class BookingSearchParametersObjectMappingFactory implements ObjectMappingFactory<BookingSearchParameters> {
        @Override
        public BookingSearchParameters instantiate(JsonReader reader) throws Exception {
            String searchText = null;
            double minPrice = 0;
            double maxPrice = 0;
            Currency currency = null;
            PriceRange priceRange = null;
            List<CatalogItemCategory> categories = null;
            double topLeftLatitude = 0;
            double topLeftLongitude = 0;
            double bottomRightLatitude = 0;
            double bottomRightLongitude = 0;
            BoundingBox boundingBox = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "text":
                        searchText = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "topLeftLatitude":
                        topLeftLatitude = reader.nextDouble();
                        break;
                    case "topLeftLongitude":
                        topLeftLongitude = reader.nextDouble();
                        break;
                    case "bottomRightLatitude":
                        bottomRightLatitude = reader.nextDouble();
                        break;
                    case "bottomRightLongitude":
                        bottomRightLongitude = reader.nextDouble();
                        break;
                    case "minPrice":
                        minPrice = reader.nextDouble();
                        break;
                    case "maxPrice":
                        maxPrice = reader.nextDouble();
                        break;
                    case "currency":
                        String code = reader.nextString();
                        currency = Currency.getInstance(code);
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

            Range<Double> range = new Range<>(minPrice, maxPrice);

            priceRange = new PriceRange(range, currency);

            Coordinate topLeftCoordinate = new Coordinate(topLeftLatitude, topLeftLongitude);
            Coordinate bottomRightCoordinate = new Coordinate(bottomRightLatitude, bottomRightLongitude);
            boundingBox = new BoundingBox(topLeftCoordinate, bottomRightCoordinate);

            return new BookingSearchParameters(searchText, priceRange, categories, boundingBox);
        }
    }

}
