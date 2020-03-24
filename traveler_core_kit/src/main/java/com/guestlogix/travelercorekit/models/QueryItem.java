package com.guestlogix.travelercorekit.models;

import android.util.Log;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;

public class QueryItem implements CatalogItem<SearchQuery> {
    private String title;
    private String subTitle;
    private URL imageURL;
    private QueryType type;
    private SearchQuery searchQuery;

    QueryItem(
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

    @Override
    public SearchQuery getItemResource() {
        return searchQuery;
    }

    public QueryType getType() {
        return type;
    }

    /***
     *returns null if the query type is unknown
     */
    static class QueryItemObjectMappingFactory implements ObjectMappingFactory<QueryItem> {
        @Override
        public QueryItem instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            QueryType type = QueryType.fromString(jsonObject.getString("type"));
            SearchQuery searchQuery = null;
            switch (type) {
                case BOOKING:
                    BookingItemSearchParameters bookingItemSearchParameters =
                            new BookingItemSearchParameters.BookingItemSearchParametersObjectMappingFactory()
                                    .instantiate(jsonObject.getJSONObject("searchParams").toString());
                    searchQuery = new BookingItemQuery(bookingItemSearchParameters);
                    break;
                case PARKING:
                    ParkingItemSearchParameters parkingItemSearchParameters =
                            new ParkingItemSearchParameters.ParkingItemSearchParametersObjectMappingFactory()
                                    .instantiate(jsonObject.getJSONObject("searchParams").toString());
                    searchQuery = new ParkingItemQuery(parkingItemSearchParameters);
                    break;
                default:
                    Log.i("AnyQuery", "unknown query item skipped: " + jsonObject.getString("type"));
                    // if you don't recognize the type return null
                    return null;
            }

            String title = jsonObject.getNullableString("title");
            String subTitle = jsonObject.getNullableString("subTitle");

            URL thumbnail = null;
            if (!jsonObject.isNull("thumbnail"))
                thumbnail = new URL(jsonObject.getString("thumbnail"));

            return new QueryItem(title, subTitle, thumbnail, type, searchQuery);
        }
    }

}
