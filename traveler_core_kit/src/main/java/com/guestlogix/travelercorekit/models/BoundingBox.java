package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public class BoundingBox implements Serializable {
    private Coordinate topLeftCoordinate;
    private Coordinate bottomRightCoordinate;

    public BoundingBox(Coordinate topLeftCoordinate, Coordinate bottomRightCoordinate) {
        this.topLeftCoordinate = topLeftCoordinate;
        this.bottomRightCoordinate = bottomRightCoordinate;
    }

    public boolean isEqual(BoundingBox boundingBox) {
        return this.topLeftCoordinate == boundingBox.topLeftCoordinate
                && this.bottomRightCoordinate == boundingBox.bottomRightCoordinate;
    }

    public Coordinate getTopLeftCoordinate() {
        return topLeftCoordinate;
    }

    public Coordinate getBottomRightCoordinate() {
        return bottomRightCoordinate;
    }
}
