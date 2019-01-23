package com.guestlogix.traveleruikit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveleruikit.R;

public class LabelValueRecyclerViewAdapter extends RecyclerView.Adapter<LabelValueRecyclerViewAdapter.ViewHolder> {

    /*
    *
    * Design: I did not put any specific click listeners within the fragment to enable calling classes to better
    *         decide which component they want to enable/add listeners to.
    *         Use case example, with provider information locations. You don't want to add a listener to the address
    *         string, because a user might want to copy it, but you do want to add a click listener to 'See in maps'
    *         so you can open the the specific location with the appropriate intent.
    */

    private LabelValueMappingAdapter mappingAdapter;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_label_value, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (null != mappingAdapter) {

            holder.label.setTag(position);
            holder.value.setTag(position);

            mappingAdapter.bindLabel(holder.label, position);
            mappingAdapter.bindValue(holder.value, position);
        }
    }

    @Override
    public int getItemCount() {
        if (null != mappingAdapter) {
            return mappingAdapter.getItemCount();
        }
        return 0;
    }

    public void setMappingAdapter(LabelValueMappingAdapter mappingAdapter) {
        this.mappingAdapter = mappingAdapter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView label;
        TextView value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
            label = mView.findViewById(R.id.itemLabel);
            value = mView.findViewById(R.id.itemValue);
        }
    }

    public interface LabelValueMappingAdapter {
        void bindLabel(TextView label, int position);
        void bindValue(TextView value, int position);
        int getItemCount();
    }
}
