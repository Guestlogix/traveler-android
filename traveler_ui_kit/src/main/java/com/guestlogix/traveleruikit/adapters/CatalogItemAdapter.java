package com.guestlogix.traveleruikit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;

public class CatalogItemAdapter extends RecyclerView.Adapter<CatalogItemAdapter.CatalogItemViewHolder> {

    @NonNull
    @Override
    public CatalogItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_catalog_section_item, parent, false);
        return new CatalogItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CatalogItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CatalogItemViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView subTitleTextView;

        public CatalogItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            thumbnailImageView = mView.findViewById(R.id.thumbnailImageView);
            titleTextView = mView.findViewById(R.id.titleTextView);
            subTitleTextView = mView.findViewById(R.id.subTitleTextView);

        }
    }
}
