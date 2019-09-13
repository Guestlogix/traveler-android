package com.guestlogix.traveleruikit.widgets;

import org.junit.Assert;
import org.junit.Test;

public class CircleIndicatorRecyclerViewDecorationTest {

    @Test
    public void calculateXPosition() {
        // given
        int indicatorItemWidth = 10;
        int indicatorItemPadding = 5;
        int recyclerViewWidth = 200;
        int itemCount = 3;

        //when
        float result = CircleIndicatorRecyclerViewDecoration.calculateXPosition(indicatorItemWidth, indicatorItemPadding, recyclerViewWidth, itemCount);

        //then the indicator should start at x = 80 to ensure centering
        float expected = 80F;
        Assert.assertTrue("Expected:" + expected + " actual:" + result, (expected - result) < 0.01);
    }

    @Test
    public void calculateYPosition() {
        // given
        int indicatorHeight = 4;
        int recyclerViewHeight = 16;

        //when
        float result = CircleIndicatorRecyclerViewDecoration.calculateYPosition(recyclerViewHeight, indicatorHeight);

        //then the indicator should start at y = 12 to ensure centering
        float expected = 12;
        Assert.assertTrue("Expected:" + expected + " actual:" + result, (expected - result) < 0.01);
    }
}