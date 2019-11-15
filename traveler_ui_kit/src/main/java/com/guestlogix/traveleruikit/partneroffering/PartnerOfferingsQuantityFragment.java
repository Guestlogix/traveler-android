package com.guestlogix.traveleruikit.partneroffering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.callbacks.FetchPurchaseFormCallback;
import com.guestlogix.travelercorekit.models.PartnerOffering;
import com.guestlogix.travelercorekit.models.PartnerOfferingItem;
import com.guestlogix.travelercorekit.models.ProductOffering;
import com.guestlogix.travelercorekit.models.PurchaseForm;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.QuestionsActivity;

import java.io.Serializable;
import java.util.List;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.REQUEST_CODE_ORDER_FLOW;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.RESULT_OK_ORDER_CONFIRMED;

public class PartnerOfferingsQuantityFragment extends Fragment {

    public static final String KEY_SELECTED_PARTNER_OFFERING_LIST = "partner_offerings";
    public static final String KEY_PARTNER_OFFERING_ITEM = "partner_offering_item";

    private int quantity = 1;
    private double unitPriceSum = 0;

    private int maximumAvailable = Integer.MAX_VALUE;

    public static PartnerOfferingsQuantityFragment newInstance(List<PartnerOffering> lstSelectedPartnerOfferings, PartnerOfferingItem partnerOfferingItem) {
        Bundle extras = new Bundle();
        extras.putSerializable(KEY_SELECTED_PARTNER_OFFERING_LIST, (Serializable) lstSelectedPartnerOfferings);
        extras.putSerializable(KEY_PARTNER_OFFERING_ITEM, partnerOfferingItem);
        PartnerOfferingsQuantityFragment partnerOfferingsFragment = new PartnerOfferingsQuantityFragment();
        partnerOfferingsFragment.setArguments(extras);
        return partnerOfferingsFragment;
    }

    private List<PartnerOffering> lstSelectedPartnerOfferings;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partner_offering_quantity, container, false);

        lstSelectedPartnerOfferings = (List<PartnerOffering>) getArguments().getSerializable(KEY_SELECTED_PARTNER_OFFERING_LIST);
        PartnerOfferingItem partnerOfferingItem = (PartnerOfferingItem) getArguments().getSerializable(KEY_PARTNER_OFFERING_ITEM);

        StringBuilder itemsListStringBuilder = new StringBuilder();
        for (PartnerOffering partnerOffering : lstSelectedPartnerOfferings) {
            itemsListStringBuilder.append("• ").append(partnerOffering.getName()).append("\n");
            if (partnerOffering.getAvailableQuantity() < maximumAvailable) {
                maximumAvailable = partnerOffering.getAvailableQuantity();
            }
        }

        ((TextView) view.findViewById(R.id.tvItemsList)).setText(itemsListStringBuilder.toString());

        TextView tvQuantity = view.findViewById(R.id.tvQuantity);
        tvQuantity.setText(String.valueOf(quantity));
        View imgLess = view.findViewById(R.id.imgLess);
        View imgMore = view.findViewById(R.id.imgMore);
        TextView tvTotalPrice = view.findViewById(R.id.tvTotalPrice);


        for (int i = 0; i < lstSelectedPartnerOfferings.size(); i++) {
            unitPriceSum += lstSelectedPartnerOfferings.get(i).getPrice().getValue(TravelerUI.getPreferredCurrency());
        }

        tvTotalPrice.setText(String.format("%s\n%s", getContext().getString(R.string.partner_offering_total_price), unitPriceSum * quantity));

        view.findViewById(R.id.imgMore).setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            tvTotalPrice.setText(String.format("%s\n%s", getContext().getString(R.string.partner_offering_total_price), unitPriceSum * quantity));
            if (quantity > 1) {
                imgLess.setVisibility(View.VISIBLE);
            }

            if(quantity == maximumAvailable)
            {
                imgMore.setVisibility(View.INVISIBLE);
            }
        });

        imgLess.setOnClickListener(v -> {
            quantity--;
            tvQuantity.setText(String.valueOf(quantity));
            tvTotalPrice.setText(String.format("%s\n%s", getContext().getString(R.string.partner_offering_total_price), unitPriceSum * quantity));
            if (quantity == 1) {
                imgLess.setVisibility(View.INVISIBLE);
            }

            if(quantity < maximumAvailable)
            {
                imgMore.setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.btnCheckout).setOnClickListener(v -> Traveler.fetchPurchaseForm(partnerOfferingItem.getItemResource(), (List<ProductOffering>) (List<?>) lstSelectedPartnerOfferings, new FetchPurchaseFormCallback() {
            @Override
            public void onPurchaseFormFetchSuccess(PurchaseForm purchaseForm) {
                Intent intent = new Intent(getActivity(), QuestionsActivity.class);
                intent.putExtra(QuestionsActivity.EXTRA_PURCHASE_FORM, purchaseForm);
                startActivityForResult(intent, REQUEST_CODE_ORDER_FLOW);
            }

            @Override
            public void onPurchaseFormFetchError(Error error) {
                new AlertDialog.Builder(getContext())
                        .setMessage(error.getMessage())
                        .show();
            }
        }));

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
