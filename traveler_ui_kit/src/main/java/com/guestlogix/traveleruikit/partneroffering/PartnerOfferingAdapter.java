package com.guestlogix.traveleruikit.partneroffering;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.PartnerOffering;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PartnerOfferingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OfferingItem> lstOfferingItem;

    public PartnerOfferingAdapter(List<PartnerOffering> lstPartnerOfferings) {
        lstOfferingItem = new ArrayList<>();
        for (PartnerOffering partnerOffering : lstPartnerOfferings) {
            lstOfferingItem.add(new PartnerOfferingAdapter.OfferingItem(partnerOffering));
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View offeringItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detailed_partner_offering, parent, false);
        return new OfferingViewHolder(offeringItemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        OfferingViewHolder itemHolder = (OfferingViewHolder) holder;
        OfferingItem item = lstOfferingItem.get(position);

        itemHolder.tvDetailsLess.setText(item.getPartnerOffering().getDescription());
        itemHolder.tvDetailsMore.setText(item.getPartnerOffering().getDescription());
        itemHolder.tvShowMore.setOnClickListener(v -> {
            lstOfferingItem.get(position).setIsExpanded(true);
            notifyItemChanged(position);
        });
        itemHolder.tvShowLess.setOnClickListener(v -> {
            lstOfferingItem.get(position).setIsExpanded(false);
            notifyItemChanged(position);
        });

        AssetManager.getInstance().loadImage(
                item.getPartnerOffering().getIconUrl(),
                itemHolder.imgOffer.getWidth(),
                itemHolder.imgOffer.getHeight(),
                itemHolder.imgOffer.hashCode(),
                new ImageLoader.ImageLoaderCallback() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap) {
                        itemHolder.imgOffer.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError() {
                        // Do nothing.
                    }
                });

        itemHolder.tvTitle.setText(item.getPartnerOffering().getName());
//        itemHolder.tvSubTitle.setText(item.getPartnerOffering().getPrice().getValueInBaseCurrency()+"");
        if (item.isExpanded()) {
            itemHolder.llMore.setVisibility(View.VISIBLE);
            itemHolder.llLess.setVisibility(View.GONE);
        } else {
            itemHolder.llMore.setVisibility(View.GONE);
            itemHolder.llLess.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return lstOfferingItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    class OfferingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOffer;
        TextView tvTitle;
        TextView tvSubTitle;
        TextView tvShowMore;
        TextView tvShowLess;
        TextView tvDetailsLess;
        TextView tvDetailsMore;
        LinearLayout llLess, llMore;

        OfferingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOffer = itemView.findViewById(R.id.imgOffer);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
            tvShowMore = itemView.findViewById(R.id.tvShowMore);
            tvShowLess = itemView.findViewById(R.id.tvShowLess);
            tvDetailsLess = itemView.findViewById(R.id.tvDetailsLess);
            tvDetailsMore = itemView.findViewById(R.id.tvDetailsMore);
            llLess = itemView.findViewById(R.id.llLess);
            llMore = itemView.findViewById(R.id.llMore);
        }
    }

    public static class OfferingItem {
        private boolean isExpanded = false;
        private PartnerOffering partnerOffering;

        OfferingItem(PartnerOffering partnerOffering) {
            this.partnerOffering = partnerOffering;
        }

        PartnerOffering getPartnerOffering() {
            return partnerOffering;
        }

        void setIsExpanded(boolean isExpanded) {
            this.isExpanded = isExpanded;
        }

        public boolean isExpanded() {
            return isExpanded;
        }
    }
}