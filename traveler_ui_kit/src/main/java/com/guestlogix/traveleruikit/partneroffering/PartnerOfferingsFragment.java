package com.guestlogix.traveleruikit.partneroffering;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;
import com.guestlogix.travelercorekit.models.PartnerOfferingItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.tools.AssetManager;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;

import java.io.Serializable;
import java.util.List;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.REQUEST_CODE_ORDER_FLOW;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.RESULT_OK_ORDER_CONFIRMED;

public class PartnerOfferingsFragment extends Fragment {

    public static final String KEY_PARTNER_OFFERING_GROUPS = "partner_offering_group";
    public static final String KEY_PARTNER_OFFERING_ITEM = "partner_offering_item";

    public static PartnerOfferingsFragment newInstance(List<PartnerOfferingGroup> lstPartnerOfferingGroups, PartnerOfferingItem partnerOfferingItem) {
        Bundle extras = new Bundle();
        extras.putSerializable(KEY_PARTNER_OFFERING_GROUPS, (Serializable) lstPartnerOfferingGroups);
        extras.putSerializable(KEY_PARTNER_OFFERING_ITEM, partnerOfferingItem);
        PartnerOfferingsFragment partnerOfferingsFragment = new PartnerOfferingsFragment();
        partnerOfferingsFragment.setArguments(extras);
        return partnerOfferingsFragment;
    }

    private List<PartnerOfferingGroup> lstPartnerOfferingGroups;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partner_offering, container, false);

        lstPartnerOfferingGroups = (List<PartnerOfferingGroup>) getArguments().getSerializable(KEY_PARTNER_OFFERING_GROUPS);
        PartnerOfferingItem partnerOfferingItem = (PartnerOfferingItem) getArguments().getSerializable(KEY_PARTNER_OFFERING_ITEM);

        RecyclerView recyclerView = view.findViewById(R.id.rvOfferingGroups);
        recyclerView.setNestedScrollingEnabled(false);


        double minimumMealPrice = 0;
        for (PartnerOfferingGroup partnerOfferingGroup : lstPartnerOfferingGroups) {
            minimumMealPrice += partnerOfferingGroup.getPriceStartingAt().getValue(TravelerUI.getPreferredCurrency());
        }

        ((TextView) view.findViewById(R.id.tvTitle)).setText(String.format("%s %s", getContext().getString(R.string.partner_offering_meals_from), minimumMealPrice));

        ((TextView) view.findViewById(R.id.tvPriceStartingFrom)).setText(String.format("%s\n%s", getContext().getString(R.string.partner_offering_starting_from), minimumMealPrice));

        ImageView imgOffer = view.findViewById(R.id.imgOffer);
        view.findViewById(R.id.btnSelectItems).setOnClickListener(v -> {
            Intent selectionActivityIntent = new Intent(getActivity(), PartnerOfferingSelectionActivity.class);
            selectionActivityIntent.putExtra(PartnerOfferingSelectionActivity.KEY_PARTNER_OFFERING_GROUP_LIST, (Serializable) lstPartnerOfferingGroups);
            selectionActivityIntent.putExtra(PartnerOfferingSelectionActivity.KEY_PARTNER_OFFERING_ITEM, partnerOfferingItem);
            startActivityForResult(selectionActivityIntent, REQUEST_CODE_ORDER_FLOW);
        });

        AssetManager.getInstance().loadImage(
                partnerOfferingItem.getImageUrl(),
                imgOffer.getWidth(),
                imgOffer.getHeight(),
                imgOffer.hashCode(),
                new ImageLoader.ImageLoaderCallback() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap) {
                        imgOffer.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError() {
                        // Do nothing.
                    }
                });

        recyclerView.setAdapter(new PartnerOfferingGroupAdapter(getContext(), lstPartnerOfferingGroups));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Activity activity = getActivity();
        if (requestCode == REQUEST_CODE_ORDER_FLOW && resultCode == RESULT_OK_ORDER_CONFIRMED && activity != null) {
            activity.setResult(RESULT_OK_ORDER_CONFIRMED);
            activity.finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
