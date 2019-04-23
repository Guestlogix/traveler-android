package com.guestlogix.traveleruikit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogGroup;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.utils.HorizontalSpaceItemDecoration;

public class CatalogSectionAdapter extends RecyclerView.Adapter<CatalogSectionAdapter.CatalogSectionViewHolder> {

    private Context context;
    private Catalog catalog;
    private CatalogSectionAdapterCallback catalogItemClickListener;

    public CatalogSectionAdapter(Context context) {
        this.context = context;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public void setOnCatalogItemClickListener(CatalogSectionAdapterCallback l) {
        this.catalogItemClickListener = l;
    }

    @NonNull
    @Override
    public CatalogSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_catalog_section, parent, false);
        return new CatalogSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogSectionViewHolder holder, int position) {
        holder.catalogItemAdapter.setSectionPosition(position);
        CatalogGroup group = catalog.getGroups().get(position);

        holder.sectionTitleTextView.setText(group.getTitle());
    }

    @Override
    public int getItemCount() {
        return catalog != null && catalog.getGroups() != null ? catalog.getGroups().size() : 0;
    }

    class CatalogSectionViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView sectionTitleTextView;
        RecyclerView sectionItemRecyclerView;
        CatalogItemAdapter catalogItemAdapter;

        CatalogSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            sectionTitleTextView = mView.findViewById(R.id.textView_catalog_sectionTitle);
            sectionItemRecyclerView = mView.findViewById(R.id.recyclerView_catalog_itemContainer);
            sectionItemRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));

            HorizontalSpaceItemDecoration spaceItemDecoration =
                    new HorizontalSpaceItemDecoration((int) itemView.getContext().getResources().getDimension(R.dimen.padding_xlarge));

            catalogItemAdapter = new CatalogItemAdapter();
            sectionItemRecyclerView.setAdapter(catalogItemAdapter);
            sectionItemRecyclerView.addItemDecoration(spaceItemDecoration);
        }
    }

    public interface CatalogSectionAdapterCallback {
        void onCatalogItemClick(int sectionId, int itemId);
    }

    class CatalogItemViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;

        CatalogItemViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imageView_catalogSectionItem_thumbnail);
            title = itemView.findViewById(R.id.textView_catalogSectionItem_title);
        }
    }

    public class CatalogItemAdapter extends RecyclerView.Adapter<CatalogSectionAdapter.CatalogItemViewHolder> {

        int sectionPosition;

        @NonNull
        @Override
        public CatalogSectionAdapter.CatalogItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_catalog_section, parent, false);
            return new CatalogItemViewHolder(view);
        }

        View.OnClickListener onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (Integer) v.getTag();

                if (catalogItemClickListener != null) {
                    catalogItemClickListener.onCatalogItemClick(sectionPosition, index);
                }
            }
        };

        @Override
        public void onBindViewHolder(@NonNull CatalogSectionAdapter.CatalogItemViewHolder holder, int position) {
            CatalogItem item = catalog.getGroups().get(sectionPosition).getItems().get(position);

            holder.thumbnail.setImageResource(R.color.colorPrimary);
            AssetManager.getInstance().loadImage(
                    item.getImageURL(),
                    (int) context.getResources().getDimension(R.dimen.thumbnail_width),
                    (int) context.getResources().getDimension(R.dimen.thumbnail_height),
                    holder.hashCode(),
                    new ImageLoader.ImageLoaderCallback() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap) {
                            holder.thumbnail.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError() {
                            // Do nothing.
                        }
                    });


            holder.title.setText(item.getTitle());
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(onItemClickListener);
        }

        void setSectionPosition(int sectionPosition) {
            this.sectionPosition = sectionPosition;
        }

        @Override
        public int getItemCount() {
            return catalog.getGroups().get(sectionPosition).getItems() != null ?
                    catalog.getGroups().get(sectionPosition).getItems().size() : 0;
        }
    }
}
