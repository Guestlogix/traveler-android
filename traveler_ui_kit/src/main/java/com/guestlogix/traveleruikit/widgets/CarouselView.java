package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.guestlogix.traveleruikit.R;

public class CarouselView extends FrameLayout {
    private RecyclerView recyclerView;

    public CarouselView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CarouselView(Context context) {
        super(context);
        initView();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) inflate(getContext(), R.layout.view_carousel, null);
        // RecyclerView doesn't generate layout params until a Layout Manger is added, therefore
        // we need to create a set of params
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(recyclerView, params);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        // android.support.v7.widget.LinearSnapHelper is available without androidX, see
        // http://androidappcoders.blogspot.com/2018/06/using-snaphelper-in-recyclerview.html
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new CircleIndicatorRecyclerViewDecoration(getContext()));
    }
}
