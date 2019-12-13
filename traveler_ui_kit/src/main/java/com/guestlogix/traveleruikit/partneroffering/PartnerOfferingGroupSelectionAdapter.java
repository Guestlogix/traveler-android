package com.guestlogix.traveleruikit.partneroffering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.PartnerOffering;
import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;
import com.guestlogix.traveleruikit.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartnerOfferingGroupSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PartnerOfferingGroup> lstPartnerOfferingGroups;
    private HashMap<Integer, PartnerOfferingSelectionAdapter> rowAdapters = new HashMap<>();
    private Context context;
    private ItemSelectionInGroupChangedCallback itemSelectionInGroupChangedCallback;

    public PartnerOfferingGroupSelectionAdapter(Context context, List<PartnerOfferingGroup> lstPartnerOfferingGroups, ItemSelectionInGroupChangedCallback itemSelectionInGroupChangedCallback) {
        this.context = context;
        this.lstPartnerOfferingGroups = lstPartnerOfferingGroups;
        this.itemSelectionInGroupChangedCallback = itemSelectionInGroupChangedCallback;
    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View offeringItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detailed_offering_group_selection, parent, false);
        return new OfferingGroupViewHolder(offeringItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        OfferingGroupViewHolder itemHolder = (OfferingGroupViewHolder) holder;
        PartnerOfferingGroup item = lstPartnerOfferingGroups.get(position);

        itemHolder.tvTitle.setText(item.getTitle());
        PartnerOfferingSelectionAdapter adapter = new PartnerOfferingSelectionAdapter(item, selectedPartnerOffering -> itemSelectionInGroupChangedCallback.onItemInGroupSelectionChanged());
        rowAdapters.put(position, adapter);
        itemHolder.rvOfferings.setAdapter(adapter);
        itemHolder.rvOfferings.setLayoutManager(new LinearLayoutManager(context));
    }

    public HashMap<PartnerOfferingGroup, PartnerOffering> getSelectedOfferings() {
        List<PartnerOffering> lstSelectedOfferings = new ArrayList<>();
        for (Integer key : rowAdapters.keySet()) {
            lstSelectedOfferings.add(rowAdapters.get(key).getSelectedPartnerOffering());
        }

        HashMap<PartnerOfferingGroup, PartnerOffering> pairPartnerGroupAndSelectedItem = new HashMap<>();
        for (Integer key : rowAdapters.keySet()) {
            pairPartnerGroupAndSelectedItem.put(lstPartnerOfferingGroups.get(key), rowAdapters.get(key).getSelectedPartnerOffering());
        }
        return pairPartnerGroupAndSelectedItem;
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

    public interface ItemSelectionInGroupChangedCallback {
        void onItemInGroupSelectionChanged();
    }
}