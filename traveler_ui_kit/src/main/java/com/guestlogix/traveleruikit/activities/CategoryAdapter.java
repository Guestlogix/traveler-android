package com.guestlogix.traveleruikit.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.BookingItemCategory;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<CategoryItem> lstCategories;
    Context context;

    public CategoryAdapter(Context context, ArrayList<CategoryItem> lstCategories) {
        this.lstCategories = lstCategories;
        this.context = context;
    }

    public void setCategories(ArrayList<CategoryItem> lstCategories) {
        this.lstCategories = lstCategories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        if (lstCategories.get(position).isSelected())
            holder.imgCheck.setVisibility(View.VISIBLE);
        else
            holder.imgCheck.setVisibility(View.INVISIBLE);
        holder.tvCategory.setText(lstCategories.get(position).getCategory().toString());

        holder.itemView.setOnClickListener(v -> {
            lstCategories.get(position).toggleSelection();
            notifyItemChanged(position);
        });

        try {
            AssetManager.getInstance().loadImage(
                    new URL(lstCategories.get(position).getCategoryIcon()),
                    (int) context.getResources().getDimension(R.dimen.booking_search_result_width),
                    (int) context.getResources().getDimension(R.dimen.booking_search_item_thumbnail_height),
                    holder.imgCategory.hashCode(),
                    new ImageLoader.ImageLoaderCallback() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap) {
                            holder.imgCategory.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } catch (MalformedURLException e) {
        }
    }

    @Override
    public int getItemCount() {
        return lstCategories.size();
    }

    public ArrayList<BookingItemCategory> getSelectedCategories() {
        ArrayList<BookingItemCategory> lstSelected = new ArrayList<>();
        for (CategoryItem categoryItem : lstCategories) {
            if (categoryItem.isSelected())
                lstSelected.add(categoryItem.category);
        }
        return lstSelected;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCheck;
        ImageView imgCategory;
        TextView tvCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgCheck = itemView.findViewById(R.id.imgCheck);
            this.imgCategory = itemView.findViewById(R.id.imgCategory);
            this.tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }

    static class CategoryItem {
        private String categoryIcon;
        private BookingItemCategory category;
        private boolean isSelected = false;

        public CategoryItem(String categoryIcon, BookingItemCategory category, boolean isSelected) {
            this.categoryIcon = categoryIcon;
            this.category = category;
            this.isSelected = isSelected;
        }

        public String getCategoryIcon() {
            return categoryIcon;
        }

        public BookingItemCategory getCategory() {
            return category;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public void toggleSelection() {
            isSelected = !isSelected;
        }
    }
}

