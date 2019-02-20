package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * A custom ViewPager to measure the height of its current child fragment so that parent can adjust its height accordingly.
 */
public class WrapContentViewPager extends ViewPager {

    public WrapContentViewPager(@NonNull Context context) {
        super(context);
    }

    public WrapContentViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int width = 0;

        if (getChildCount() > 0) {
            View child = getChildAt(getCurrentItem());
            child.measure(widthMeasureSpec, heightMeasureSpec);
            height = child.getMeasuredHeight();
            width = child.getMeasuredWidth();
        }

        if (height != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            setMeasuredDimension(width, height);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}