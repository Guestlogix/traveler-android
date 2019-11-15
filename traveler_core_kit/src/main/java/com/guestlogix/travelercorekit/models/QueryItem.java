package com.guestlogix.travelercorekit.models;

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

}
