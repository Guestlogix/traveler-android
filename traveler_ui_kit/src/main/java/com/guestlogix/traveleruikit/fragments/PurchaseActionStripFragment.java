package com.guestlogix.traveleruikit.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.callbacks.FetchPurchaseFormCallback;
import com.guestlogix.travelercorekit.models.BookingProduct;
import com.guestlogix.travelercorekit.models.ParkingProduct;
import com.guestlogix.travelercorekit.models.Price;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.PurchaseForm;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.activities.AvailabilityActivity;
import com.guestlogix.traveleruikit.activities.QuestionsActivity;
import com.guestlogix.traveleruikit.widgets.ActionStrip;

import java.util.Locale;

import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.REQUEST_CODE_ORDER_FLOW;
import static com.guestlogix.traveleruikit.activities.OrderConfirmationActivity.RESULT_OK_ORDER_CONFIRMED;

/**
 * Fragment to handle Product purchase click
 * - bookable: Leads to {@link AvailabilityActivity}
 * - parking: Fetches purchase form and Leads to {@link QuestionsActivity}
 */
public class PurchaseActionStripFragment extends Fragment
        implements FetchPurchaseFormCallback, View.OnClickListener {
    public static final String ARG_PRODUCT = "ARG_PRODUCT";
    public static final String TAG = "BookableActionStrip";

    private Product product;

    public static PurchaseActionStripFragment newInstance(Product product) {
        PurchaseActionStripFragment f = new PurchaseActionStripFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_action_strip, container, false);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(ARG_PRODUCT)) {
            Log.e(TAG, "No Product");
            return view;
        }

        product = (Product) args.get(ARG_PRODUCT);
        Assertion.eval(product != null);

        ActionStrip actionStrip = view.findViewById(R.id.action_container);
        actionStrip.setActionOnClickListener(this);
        Price price = product.getPrice();

        switch (product.getProductType()) {
            case PARKING:
                setActionStripParking(actionStrip, price);
                break;
            case BOOKABLE:
                setActionStripBookable(actionStrip, price);
                break;
            default:
                throw new IllegalArgumentException("unknown Product type:" + product.getProductType());
        }

        return view;
    }

    private void setActionStripBookable(ActionStrip actionStrip, Price price) {
        Context context = requireContext();
        String checkAvailability = context.getString(R.string.button_next);
        String startingAt = context.getString(R.string.label_starting_at);
        String localizedPrice = String.format(Locale.getDefault(), context.getString(R.string.label_price_per_person),
                price.getLocalizedDescription(TravelerUI.getPreferredCurrency()));

        actionStrip.setStripValues(checkAvailability, startingAt, localizedPrice);
    }

    private void setActionStripParking(ActionStrip actionStrip, Price price) {
        Context context = requireContext();
        String checkAvailability = context.getString(R.string.button_book_parking);
        String startingAt = context.getString(R.string.total_price);
        String localizedPrice = price.getLocalizedDescription(TravelerUI.getPreferredCurrency());

        actionStrip.setStripValues(checkAvailability, startingAt, localizedPrice);
    }

    @Override
    public void onClick(View v) {
        switch (product.getProductType()) {
            case PARKING:
                Traveler.fetchPurchaseForm((ParkingProduct) product, this);
                break;
            case BOOKABLE:
                Intent intent = new Intent(requireContext(), AvailabilityActivity.class);
                intent.putExtra(AvailabilityActivity.ARG_PRODUCT, product);
                startActivityForResult(intent, REQUEST_CODE_ORDER_FLOW);
                break;
            default:
                throw new IllegalArgumentException("unknown Product type:" + product.getProductType());
        }
    }

    /**
     * @param purchaseForm purchase form with questions - for parking products only
     */
    @Override
    public void onPurchaseFormFetchSuccess(PurchaseForm purchaseForm) {
        Intent intent = new Intent(requireContext(), QuestionsActivity.class);
        intent.putExtra(QuestionsActivity.EXTRA_PURCHASE_FORM, purchaseForm);
        startActivityForResult(intent, REQUEST_CODE_ORDER_FLOW);
    }

    @Override
    public void onPurchaseFormFetchError(Error error) {
        new AlertDialog.Builder(requireContext())
                .setMessage("Error fetching parking purchase form")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
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
