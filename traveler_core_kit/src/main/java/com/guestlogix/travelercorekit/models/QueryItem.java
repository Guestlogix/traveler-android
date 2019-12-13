package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;

public class QueryItem implements CatalogItem {
    private String title;
    private String subTitle;
    private URL imageURL;
    private QueryType type;
    private SearchQuery searchQuery;

    private QueryItem(
            String title,
            String subTitle,
            URL imageURL,
            QueryType type,
            SearchQuery searchQuery) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
        this.type = type;
        this.searchQuery = searchQuery;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSubtitle() {
        return subTitle;
    }

    @Override
    public URL getImageUrl() {
        return imageURL;
    }

    public QueryType getType() {
        return type;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    static class QueryItemMappingFactory implements ObjectMappingFactory<CatalogItem> {
        @Override
        public CatalogItem instantiate(JsonReader reader) throws Exception {
            String title = null;
            String subTitle = null;
            URL thumbnail = null;
            QueryType type = null;
            SearchQuery searchQuery = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "subTitle":
                        subTitle = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "thumbnail":
                        JsonToken token = reader.peek();

                        if (token == JsonToken.NULL) {
                            reader.skipValue();
                            break;
                        }

                        thumbnail = new URL(reader.nextString());
                        break;
                    case "type":
                        type = QueryType.fromString(reader.nextString());
                        break;
                    case "searchParams":
                        Assertion.eval(type != null);
                        switch (type) {
                            case BOOKING:
                                BookingItemSearchParameters bookingItemSearchParameters =
                                        new BookingItemSearchParameters.BookingItemSearchParametersObjectMappingFactory()
                                                .instantiate(reader);
                                searchQuery = new BookingItemQuery(bookingItemSearchParameters);
                                break;
                            case PARKING:
                                ParkingItemSearchParameters parkingItemSearchParameters =
                                        new ParkingItemSearchParameters.ParkingItemSearchParametersObjectMappingFactory()
                                                .instantiate(reader);
                                searchQuery = new ParkingItemQuery(parkingItemSearchParameters);
                                break;
                        }
                        break;
                    default:
                        reader.skipValue();
                        break;

                }
            }

            reader.endObject();

            return new QueryItem(title, subTitle, thumbnail, type, searchQuery);
        }
    }
}
