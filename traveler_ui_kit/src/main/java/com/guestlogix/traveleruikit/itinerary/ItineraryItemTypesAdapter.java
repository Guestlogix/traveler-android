package com.guestlogix.traveleruikit.itinerary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.ItineraryItemType;
import com.guestlogix.traveleruikit.R;

import java.util.ArrayList;
import java.util.List;

public class ItineraryItemTypesAdapter extends RecyclerView.Adapter<ItineraryItemTypesAdapter.CategoryViewHolder> {

    private ArrayList<CategoryWrapperItem> lstCategories;

    public ItineraryItemTypesAdapter(List<ItineraryItemType> lstItineraryItemTypes, ArrayList<ItineraryItemType> lstSelectedItineraryItemTypes) {
        this.lstCategories = CategoryWrapperItem.fromProductTypeList(lstItineraryItemTypes, lstSelectedItineraryItemTypes);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryItemTypesAdapter.CategoryViewHolder holder, int position) {
        if (lstCategories.get(position).isSelected())
            holder.imgCheck.setVisibility(View.VISIBLE);
        else
            holder.imgCheck.setVisibility(View.INVISIBLE);

        holder.tvCategory.setText(lstCategories.get(position).getCategory().toString());

        holder.itemView.setOnClickListener(v -> {
            lstCategories.get(position).toggleSelection();
            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return lstCategories.size();
    }

    public ArrayList<ItineraryItemType> getSelectedCategories() {
        ArrayList<ItineraryItemType> lstSelected = new ArrayList<>();
        for (CategoryWrapperItem categoryItem : lstCategories) {
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

    static class CategoryWrapperItem {
        private ItineraryItemType category;
        private boolean isSelected = false;

        public CategoryWrapperItem(ItineraryItemType category, boolean isSelected) {
            this.category = category;
            this.isSelected = isSelected;
        }

        public ItineraryItemType getCategory() {
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

        public static ArrayList<CategoryWrapperItem> fromProductTypeList(List<ItineraryItemType> lstProductTypes, List<ItineraryItemType> lstSelectedTypes) {
            ArrayList<CategoryWrapperItem> categoryWrapperItems = new ArrayList<>();
            for (ItineraryItemType itineraryItemType : lstProductTypes) {
                categoryWrapperItems.add(new CategoryWrapperItem(itineraryItemType, lstSelectedTypes.contains(itineraryItemType)));
            }
            return categoryWrapperItems;
        }
    }
}

