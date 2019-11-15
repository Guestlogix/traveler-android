package com.guestlogix.travelercorekit.models;

public enum CatalogItemType {
    ITEM, QUERY, UNKNOWN;

    public static CatalogItemType fromString(String value) throws IllegalArgumentException {
        switch (value) {
            case "Item":
                return ITEM;
            case "Query":
                return QUERY;
            default:
                return UNKNOWN;
        }
    }
}
