package com.guestlogix.traveleruikit.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.traveleruikit.adapters.CatalogSectionAdapter;

public class CatalogWidget extends RecyclerView implements CatalogSectionAdapter.CatalogSectionAdapterCallback {

    protected LinearLayoutManager layoutManager;
    protected CatalogSectionAdapter catalogSectionAdapter;

    /**
     * Listener used to dispatch catalog item click events.
     */
    private OnCatalogItemClickListener catalogItemClickListener;

    public CatalogWidget(@NonNull Context context) {
        super(context);
    }

    public CatalogWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
    }

    public CatalogWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (isInEditMode()) {
            return;
        } else {
            catalogSectionAdapter = new CatalogSectionAdapter(context);
            catalogSectionAdapter.setOnCatalogItemClickListener(this);
            layoutManager = new LinearLayoutManager(context);
            setLayoutManager(layoutManager);
            setAdapter(catalogSectionAdapter);
        }
    }

    /**
     * Sets the catalog and re-renders the widget.
     *
     * @param catalog Catalog to be rendered
     */
    public void setCatalog(Catalog catalog) {
        catalogSectionAdapter.setCatalog(catalog);
        catalogSectionAdapter.notifyDataSetChanged();
    }

    /**
     * Registers a callback to be invoked whenever the catalog is clicked.
     *
     * @param l The callback that will run
     */
    public void setOnCatalogItemClickListener(OnCatalogItemClickListener l) {
        this.catalogItemClickListener = l;
    }

    @Override
    public void onCatalogItemClick(int sectionId, int itemId) {
        if (catalogItemClickListener != null) {
            catalogItemClickListener.onCatalogItemClick(sectionId, itemId);
        }
    }

    /**
     * Interface definition for a callback of Catalog item clicks.
     */
    public interface OnCatalogItemClickListener {
        /**
         * Called whenever a catalog item is clicked.
         *
         * @param section section where the click happened
         * @param item    item which was clicked
         */
        void onCatalogItemClick(int section, int item);
    }
}
