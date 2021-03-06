package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class CatalogGroup implements Serializable {
    private String title;
    private String subTitle;
    private boolean isFeatured;
    private CatalogItemType itemType;
    private List<CatalogItem> items;
    private QueryItem seeAllQuery;

    CatalogGroup(
            String title,
            String subTitle,
            boolean isFeatured,
            CatalogItemType itemType,
            @NonNull List<CatalogItem> items,
            QueryItem seeAllQuery) {
        this.title = title;
        this.subTitle = subTitle;
        this.isFeatured = isFeatured;
        this.itemType = itemType;
        this.items = items;
        this.seeAllQuery = seeAllQuery;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public CatalogItemType getItemType() {
        return itemType;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public QueryItem getSeeAllQuery() {
        return seeAllQuery;
    }
}