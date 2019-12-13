package com.guestlogix.traveleruikit.partneroffering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;
import com.guestlogix.traveleruikit.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PartnerOfferingGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PartnerOfferingGroup> lstPartnerOfferingGroups;
    private Context context;

    public PartnerOfferingGroupAdapter(Context context, List<PartnerOfferingGroup> lstPartnerOfferingGroups) {
        this.context = context;
        this.lstPartnerOfferingGroups = lstPartnerOfferingGroups;
    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View offeringItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detailed_offering_group, parent, false);
        return new OfferingGroupViewHolder(offeringItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        OfferingGroupViewHolder itemHolder = (OfferingGroupViewHolder) holder;
        PartnerOfferingGroup item = lstPartnerOfferingGroups.get(position);

        itemHolder.tvTitle.setText(item.getTitle());
        itemHolder.rvOfferings.setAdapter(new PartnerOfferingAdapter(item.getOfferings()));
        itemHolder.rvOfferings.setLayoutManager(new LinearLayoutManager(context));
    }


    @Override
    public int getItemCount() {
        return lstPartnerOfferingGroups.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    class OfferingGroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RecyclerView rvOfferings;


        OfferingGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            rvOfferings = itemView.findViewById(R.id.rvOfferings);
            rvOfferings.setNestedScrollingEnabled(false);
        }
    }

}