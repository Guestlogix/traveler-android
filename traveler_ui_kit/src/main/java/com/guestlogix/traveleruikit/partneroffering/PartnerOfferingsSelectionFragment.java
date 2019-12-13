package com.guestlogix.traveleruikit.partneroffering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.PartnerOffering;
import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;
import com.guestlogix.travelercorekit.models.PartnerOfferingItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.REQUEST_CODE_ORDER_FLOW;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.RESULT_OK_ORDER_CONFIRMED;

public class PartnerOfferingsSelectionFragment extends Fragment implements PartnerOfferingGroupSelectionAdapter.ItemSelectionInGroupChangedCallback {

    public static final String KEY_PARTNER_OFFERING_GROUP = "partner_offering_group";
    public static final String KEY_PARTNER_OFFERING_ITEM = "partner_offering_item";


    public static PartnerOfferingsSelectionFragment newInstance(List<PartnerOfferingGroup> lstPartnerOfferingGroups, PartnerOfferingItem partnerOfferingItem) {
        Bundle extras = new Bundle();
        extras.putSerializable(KEY_PARTNER_OFFERING_GROUP, (Serializable) lstPartnerOfferingGroups);
        extras.putSerializable(KEY_PARTNER_OFFERING_ITEM, partnerOfferingItem);
        PartnerOfferingsSelectionFragment partnerOfferingsFragment = new PartnerOfferingsSelectionFragment();
        partnerOfferingsFragment.setArguments(extras);
        return partnerOfferingsFragment;
    }

    private Button btnSelectQuantity;
    private TextView tvTotalPrice;
    private PartnerOfferingGroupSelectionAdapter adapter;
    private double minimumMealPrice = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partner_offering_selection, container, false);

        List<PartnerOfferingGroup> lstPartnerOfferingGroups = (List<PartnerOfferingGroup>) getArguments().getSerializable(KEY_PARTNER_OFFERING_GROUP);
        PartnerOfferingItem partnerOfferingItem = (PartnerOfferingItem) getArguments().getSerializable(KEY_PARTNER_OFFERING_ITEM);

        btnSelectQuantity = view.findViewById(R.id.btnSelectQuantity);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);


        for (PartnerOfferingGroup partnerOfferingGroup : lstPartnerOfferingGroups) {
            minimumMealPrice += partnerOfferingGroup.getPriceStartingAt().getValue(TravelerUI.getPreferredCurrency());
        }

        tvTotalPrice.setText(String.format("%s\n%s", getContext().getString(R.string.partner_offering_total_price), minimumMealPrice));


        btnSelectQuantity.setOnClickListener(v -> {
            Intent quantityActivityIntent = new Intent(getActivity(), PartnerOfferingQuantityActivity.class);
            quantityActivityIntent.putExtra(PartnerOfferingQuantityActivity.KEY_SELECTED_PARTNER_OFFERINGS_LIST, (Serializable) new ArrayList<>(adapter.getSelectedOfferings().values()));
            quantityActivityIntent.putExtra(PartnerOfferingQuantityActivity.KEY_PARTNER_OFFERING_ITEM, partnerOfferingItem);
            startActivityForResult(quantityActivityIntent, REQUEST_CODE_ORDER_FLOW);
        });

        RecyclerView recyclerView = view.findViewById(R.id.rvOfferingGroups);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setItemViewCacheSize(10);
        adapter = new PartnerOfferingGroupSelectionAdapter(getContext(), lstPartnerOfferingGroups, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onItemInGroupSelectionChanged() {

        HashMap<PartnerOfferingGroup, PartnerOffering> selectedOfferingsAndGroupPair = adapter.getSelectedOfferings();
        boolean isOneSelectedFromEachGroup = !selectedOfferingsAndGroupPair.values().contains(null);
        btnSelectQuantity.setEnabled(isOneSelectedFromEachGroup);

        double extras = 0;
        for (Map.Entry<PartnerOfferingGroup, PartnerOffering> entry : selectedOfferingsAndGroupPair.entrySet()) {
            if (entry.getValue() != null) {
                double groupExtra = entry.getValue().getPrice().getValue(TravelerUI.getPreferredCurrency()) - entry.getKey().getPriceStartingAt().getValue(TravelerUI.getPreferredCurrency());
                extras += groupExtra;
            }

        }

        tvTotalPrice.setText(String.format("%s\n%s", getContext().getString(R.string.partner_offering_total_price), minimumMealPrice + extras));
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
