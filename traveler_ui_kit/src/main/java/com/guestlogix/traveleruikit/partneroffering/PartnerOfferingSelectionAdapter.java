package com.guestlogix.traveleruikit.partneroffering;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.PartnerOffering;
import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;

import org.jetbrains.annotations.NotNull;

public class PartnerOfferingSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private PartnerOfferingGroup partnerOfferingGroup;
    private int selectedIndex = -1;
    private ItemSelectionChangeCallback itemSelectionChangeCallback;

    public PartnerOfferingSelectionAdapter(PartnerOfferingGroup partnerOfferingGroup, ItemSelectionChangeCallback itemSelectionChangeCallback) {
        this.partnerOfferingGroup = partnerOfferingGroup;
        this.itemSelectionChangeCallback = itemSelectionChangeCallback;
    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View offeringItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partner_offering_selection, parent, false);
        return new OfferingViewHolder(offeringItemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        OfferingViewHolder itemHolder = (OfferingViewHolder) holder;
        PartnerOffering item = partnerOfferingGroup.getOfferings().get(position);

        itemHolder.tvTitle.setText(item.getName());
        if (selectedIndex == position) {
            itemHolder.imgSelected.setVisibility(View.VISIBLE);
        } else {
            itemHolder.imgSelected.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (selectedIndex == position)
                return;

            int oldSelectedIndex = selectedIndex;
            selectedIndex = position;

            notifyItemChanged(oldSelectedIndex);
            notifyItemChanged(position);
            itemSelectionChangeCallback.onItemSelectionChanged(item);
        });

        double extra = item.getPrice().getValue(TravelerUI.getPreferredCurrency()) - partnerOfferingGroup.getPriceStartingAt().getValue(TravelerUI.getPreferredCurrency());
        if (extra > 0) {
            itemHolder.tvExtra.setVisibility(View.VISIBLE);
            itemHolder.tvExtra.setText(String.format("+%s", extra));
        }
        else {
            itemHolder.tvExtra.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return partnerOfferingGroup.getOfferings().size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public PartnerOffering getSelectedPartnerOffering() {
        if (selectedIndex == -1)
            return null;
        return partnerOfferingGroup.getOfferings().get(selectedIndex);
    }


    class OfferingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSelected;
        TextView tvTitle;
        TextView tvExtra;

        OfferingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSelected = itemView.findViewById(R.id.imgSelected);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvExtra = itemView.findViewById(R.id.tvExtra);
        }
    }

    public interface ItemSelectionChangeCallback {
        void onItemSelectionChanged(PartnerOffering selectedPartnerOffering);
    }
}
