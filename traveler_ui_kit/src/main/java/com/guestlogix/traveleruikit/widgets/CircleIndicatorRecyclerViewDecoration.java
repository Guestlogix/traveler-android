package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;

/**
 * Inspired by @lbgupta's answer at https://stackoverflow.com/a/48071124
 * Original code licenced under CC BY-SA 4.0 https://creativecommons.org/licenses/by-sa/4.0/
 *
 * Terminology:
 *
 * Indicator: the series of white/ grey circles placed near the recyclerView,
 * that shows the cardinality of the first currently visible item
 *
 * Circle: each circle, corresponding to each item in the recyclerView adapter.
 */
public class CircleIndicatorRecyclerViewDecoration extends RecyclerView.ItemDecoration {
    private float indicatorHeight;
    private float circleDiameter;
    private float circlePadding;
    private int activeColor;
    private int inactiveColor;

    private final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private final Paint paint = new Paint();

    CircleIndicatorRecyclerViewDecoration(Context context) {
        Resources resources = context.getResources();
        indicatorHeight = resources.getDimension(R.dimen.catalog_item_detail_indicator_height);
        circleDiameter = (int) resources.getDimension(R.dimen.catalog_item_detail_item_width);
        circlePadding = (int) resources.getDimension(R.dimen.catalog_item_detail_item_padding);
        activeColor = ContextCompat.getColor(context, R.color.catalog_item_detail_indicator_active);
        inactiveColor = ContextCompat.getColor(context, R.color.catalog_item_detail_indicator_inactive);

        paint.setStrokeWidth(resources.getDimension(R.dimen.catalog_item_detail_stroke_width));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        if (null == recyclerView.getAdapter() || recyclerView.getAdapter().getItemCount() < 1) { return; }

        int itemCount = recyclerView.getAdapter().getItemCount();
        float indicatorStartX = calculateXPosition(circleDiameter, circlePadding, recyclerView.getWidth(), itemCount);
        float indicatorStartY = calculateYPosition(recyclerView.getHeight(), indicatorHeight);

        drawInactiveCircles(canvas, indicatorStartX, indicatorStartY, itemCount);

        // find active page (which should be highlighted)
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (null == layoutManager) { return; }
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) { return; }

        // find offset of first visible position page (i.e. the user is scrolling)
        View activeView = layoutManager.findViewByPosition(activePosition);
        if (null == activeView) { return; }

        float left = activeView.getLeft();
        float width = activeView.getWidth();
        // on swipe the active item will be positioned from [-width, 0]
        float swipeProgress = interpolator.getInterpolation(left * -1 / width);

        drawActiveCircle(canvas, indicatorStartX, indicatorStartY, activePosition, swipeProgress);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = (int) indicatorHeight;
    }

    private void drawInactiveCircles(Canvas c, float indicatorStartX, float yPosition, int itemCount) {
        paint.setColor(inactiveColor);
        // width of item indicator including padding
        float itemWidth = circleDiameter + circlePadding;
        float xPosition = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            c.drawCircle(xPosition, yPosition, circleDiameter / 2F, paint);
            xPosition += itemWidth;
        }
    }

    /**
     * Draws the active circle;
     * if swipeProgress is non-zero then active dot will be drawn with an offset
     * @param initialXPosition starting x-position of the indicator bar
     * @param initialYPosition starting x-position of the indicator bar
     */
    private void drawActiveCircle(Canvas canvas, float initialXPosition, float initialYPosition,
                                  int activePosition, float swipeProgress) {
        if (activePosition < 0) { return; }
        paint.setColor(activeColor);

        float itemWidth = circleDiameter + circlePadding;
        float highlightStart = initialXPosition + itemWidth * activePosition;
        float offset = 0F;
        if (swipeProgress > 0 && swipeProgress < 1) {
            offset = circleDiameter * swipeProgress + circlePadding * swipeProgress;
        }
        canvas.drawCircle(highlightStart + offset, initialYPosition, circleDiameter / 2F, paint);
    }

    /**
     * Calculates the position at which the left side of the indicator bar should be drawn
     *
     * @param indicatorItemWidth   Width of each individual indicator (px)
     * @param indicatorItemPadding Width of the spacing between indicators (px)
     * @param recyclerViewWidth    Width of the screen area given to the recyclerView (px)
     * @param itemCount            Number of items added to the recyclerView's adapter
     */
    static float calculateXPosition(float indicatorItemWidth, float indicatorItemPadding,
                                            int recyclerViewWidth, int itemCount) {

        float circleWidthSum = indicatorItemWidth * itemCount;
        float circlesPaddingSum = Math.max(0, itemCount - 1) * indicatorItemPadding;
        float indicatorTotalWidth = circleWidthSum + circlesPaddingSum;

        // the series of circles should be centered horizontally
        return (int) ((recyclerViewWidth - indicatorTotalWidth) / 2F);
    }


    /**
     * Calculates the position at which the left side of the indicator bar should be drawn
     * @param recyclerViewHeight Height of the screen area given to the recyclerView (px)
     * @param indicatorHeight Height of the screen area given to the
     */
    static float calculateYPosition(int recyclerViewHeight, float indicatorHeight) {
        return recyclerViewHeight - indicatorHeight / 2F;
    }
}
