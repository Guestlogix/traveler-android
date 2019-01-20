package com.guestlogix.traveleruikit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.widgets.CatalogView;

public class CatalogSectionAdapter extends RecyclerView.Adapter<CatalogSectionAdapter.CatalogSectionViewHolder> {

    CatalogView.CatalogViewAdapter catalogViewAdapter;

    public void setCatalogViewAdapter(CatalogView.CatalogViewAdapter catalogViewAdapter) {
        this.catalogViewAdapter = catalogViewAdapter;
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

        holder.seeAllTextView.setTag(position);
        holder.seeAllTextView.setOnClickListener(onSeeAllClickListener);
        holder.catalogItemAdapter.setSectionPosition(position);
        catalogViewAdapter.onBindSection(position, holder.sectionTitleTextView);
    }

    @Override
    public int getItemCount() {
        return catalogViewAdapter.getSectionsCount();
    }

    public class CatalogSectionViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView sectionTitleTextView;
        TextView seeAllTextView;
        RecyclerView sectionItemRecyclerView;
        CatalogItemAdapter catalogItemAdapter;

        public CatalogSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            sectionTitleTextView = mView.findViewById(R.id.sectionTitleTextView);
            seeAllTextView = mView.findViewById(R.id.seeAllTextView);
            sectionItemRecyclerView = mView.findViewById(R.id.sectionItemRecyclerView);
            sectionItemRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));

            catalogItemAdapter = new CatalogItemAdapter();
            sectionItemRecyclerView.setAdapter(catalogItemAdapter);

        }
    }

    View.OnClickListener onSeeAllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();
            catalogViewAdapter.onSeeAllClick(index);
        }
    };

    public class CatalogItemAdapter extends RecyclerView.Adapter<CatalogSectionAdapter.CatalogItemViewHolder> {

        int sectionPosition;

        @NonNull
        @Override
        public CatalogSectionAdapter.CatalogItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_catalog_section_item, parent, false);
            return new CatalogItemViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull CatalogSectionAdapter.CatalogItemViewHolder holder, int position) {
            catalogViewAdapter.onBindItem(sectionPosition, position, holder.thumbnailImageView, holder.titleTextView, holder.subTitleTextView);

            holder.mView.setTag(position);
            holder.mView.setOnClickListener(onItemClickListener);
        }

        @Override
        public int getItemCount() {
            return catalogViewAdapter.getSectionItemsCount(sectionPosition);
        }

        void setSectionPosition(int sectionPosition) {
            this.sectionPosition = sectionPosition;
        }

        View.OnClickListener onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (Integer) v.getTag();
                catalogViewAdapter.onItemClick(sectionPosition,index);
            }
        };
    }

    class CatalogItemViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView subTitleTextView;

        CatalogItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            thumbnailImageView = mView.findViewById(R.id.thumbnailImageView);
            titleTextView = mView.findViewById(R.id.titleTextView);
            subTitleTextView = mView.findViewById(R.id.subTitleTextView);

        }
    }


}
