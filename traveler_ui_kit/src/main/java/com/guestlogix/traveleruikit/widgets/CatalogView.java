package com.guestlogix.traveleruikit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.CatalogSectionAdapter;

public class CatalogView extends FrameLayout {

    RecyclerView catalogRecyclerView;
    CatalogSectionAdapter catalogSectionAdapter;
    CatalogViewAdapter catalogViewAdapter;

    public CatalogView(@NonNull Context context) {
        super(context);
    }

    public CatalogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
    }

    public CatalogView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CatalogView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (isInEditMode()) {
            return;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.view_catalog, this, true);
            catalogRecyclerView = view.findViewById(R.id.catalogRecyclerView);
        }
    }

    public void setCatalogViewAdapter(CatalogViewAdapter catalogViewAdapter) {
        this.catalogViewAdapter = catalogViewAdapter;

        catalogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        catalogSectionAdapter = new CatalogSectionAdapter();
        catalogSectionAdapter.setCatalogViewAdapter(catalogViewAdapter);
        catalogRecyclerView.setAdapter(catalogSectionAdapter);
    }

    public interface CatalogViewAdapter {
        void onBindSection(int sectionPosition, TextView titleTextView);

        void onBindItem(int sectionPosition, int itemIndex, int holderId, ImageView thumbNailImageView, TextView titleTextView, TextView subTitleTextView);

        void onSeeAllClick(int sectionPosition);

        void onItemClick(int sectionPosition, int itemIndex);

        int getSectionsCount();

        int getSectionItemsCount(int sectionIndex);
    }
}
