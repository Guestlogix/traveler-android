package com.guestlogix.traveleruikit.partneroffering;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.models.PartnerOffering;
import com.guestlogix.travelercorekit.models.PartnerOfferingItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

import java.io.Serializable;
import java.util.List;

public class PartnerOfferingQuantityActivity extends AppCompatActivity {

    public static final String KEY_SELECTED_PARTNER_OFFERINGS_LIST = "selected_partner_offerings_list";
    public static final String KEY_PARTNER_OFFERING_ITEM = "partner_offering_item";


    private List<PartnerOffering> lstPartnerOfferings = null;
    private PartnerOfferingItem partnerOfferingItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_partner_offerings_quantity);
        FragmentTransactionQueue transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        Serializable serializableExtra = getIntent().getSerializableExtra(KEY_SELECTED_PARTNER_OFFERINGS_LIST);

        if (serializableExtra instanceof List) {
            lstPartnerOfferings = (List<PartnerOffering>) serializableExtra;
        }

        if (lstPartnerOfferings == null) {
            throw new IllegalArgumentException("selected partner offering group list must be provided to the bundle of this activity");
        }

        serializableExtra = getIntent().getSerializableExtra(KEY_PARTNER_OFFERING_ITEM);

        if (serializableExtra instanceof PartnerOfferingItem) {
            partnerOfferingItem = (PartnerOfferingItem) serializableExtra;
        }

        if (partnerOfferingItem == null) {
            throw new IllegalArgumentException("partner offering item must be provided to the bundle of this activity");
        }

        Fragment loadingFragment = PartnerOfferingsQuantityFragment.newInstance(lstPartnerOfferings, partnerOfferingItem);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.quantity_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

    }
}
