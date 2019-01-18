package com.guestlogix.traveleruikit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
                .inflate(R.layout.view_catalog_section, parent, false);
        return new CatalogSectionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CatalogSectionViewHolder holder, int position) {

        holder.seeAllTextView.setTag(position);
        holder.seeAllTextView.setOnClickListener(onSeeAllClickListener);

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

        public CatalogSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            sectionTitleTextView = mView.findViewById(R.id.sectionTitleTextView);
            seeAllTextView = mView.findViewById(R.id.seeAllTextView);
            sectionItemRecyclerView = mView.findViewById(R.id.sectionItemRecyclerView);

        }
    }

    View.OnClickListener onSeeAllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();
            catalogViewAdapter.onSeeAllClick(index);
        }
    };
}
