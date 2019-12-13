package com.guestlogix.traveleruikit.partneroffering;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;
import com.guestlogix.travelercorekit.models.PartnerOfferingItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

import java.io.Serializable;
import java.util.List;

public class PartnerOfferingSelectionActivity extends AppCompatActivity {

    public static final String KEY_PARTNER_OFFERING_GROUP_LIST = "partner_offering_group_list";
    public static final String KEY_PARTNER_OFFERING_ITEM = "partner_offering_item";

    private List<PartnerOfferingGroup> lstPartnerOfferingGroups = null;
    private PartnerOfferingItem partnerOfferingItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_partner_offerings_selection);
        FragmentTransactionQueue transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        Serializable serializableExtra = getIntent().getSerializableExtra(KEY_PARTNER_OFFERING_GROUP_LIST);

        if (serializableExtra instanceof List) {
            lstPartnerOfferingGroups = (List<PartnerOfferingGroup>) serializableExtra;
        }

        if (lstPartnerOfferingGroups == null) {
            throw new IllegalArgumentException("partner offering group list must be provided to the bundle of this activity");
        }

        serializableExtra = getIntent().getSerializableExtra(KEY_PARTNER_OFFERING_ITEM);

        if (serializableExtra instanceof PartnerOfferingItem) {
            partnerOfferingItem = (PartnerOfferingItem) serializableExtra;
        }

        if (partnerOfferingItem == null) {
            throw new IllegalArgumentException("partner offering item must be provided to the bundle of this activity");
        }

        Fragment loadingFragment = PartnerOfferingsSelectionFragment.newInstance(lstPartnerOfferingGroups, partnerOfferingItem);
        FragmentTransaction transaction = transactionQueue.newTransaction();
        transaction.replace(R.id.partner_offering_container, loadingFragment);
        transactionQueue.addTransaction(transaction);

    }
}
