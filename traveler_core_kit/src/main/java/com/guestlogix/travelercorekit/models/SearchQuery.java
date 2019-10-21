package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public interface SearchQuery extends Serializable {
    BoundingBox getBoundingBox();
    int getOffset();
    int getLimit();
}
