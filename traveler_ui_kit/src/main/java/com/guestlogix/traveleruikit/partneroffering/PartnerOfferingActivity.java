package com.guestlogix.traveleruikit.partneroffering;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.travelercorekit.callbacks.PartnerOfferingFetchCallback;
import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;
import com.guestlogix.travelercorekit.models.PartnerOfferingItem;
import com.guestlogix.travelercorekit.models.Traveler;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.utils.FragmentTransactionQueue;

import java.io.Serializable;
import java.util.List;

public class PartnerOfferingActivity extends AppCompatActivity {

    public static final String KEY_PARTNER_OFFERING_ITEM = "partner_offering_item";
    private FragmentTransactionQueue transactionQueue;
    private PartnerOfferingItem partnerOfferingItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_partner_offerings);
        this.transactionQueue = new FragmentTransactionQueue(getSupportFragmentManager());

        Serializable serializableExtra = getIntent().getSerializableExtra(KEY_PARTNER_OFFERING_ITEM);

        if (serializableExtra instanceof PartnerOfferingItem) {
            partnerOfferingItem = (PartnerOfferingItem) serializableExtra;
        }

        if (partnerOfferingItem == null) {
            throw new IllegalArgumentException("partner offering must be provided to the bundle of this activity");
        }

        Traveler.fetchPartnerOfferings(partnerOfferingItem.getId(), new PartnerOfferingFetchCallback() {
            @Override
            public void onSuccess(List<PartnerOfferingGroup> lstPartnerOfferingGroups) {
                Fragment loadingFragment = PartnerOfferingsFragment.newInstance(lstPartnerOfferingGroups, partnerOfferingItem);
                FragmentTransaction transaction = transactionQueue.newTransaction();
                transaction.replace(R.id.partner_offering_container, loadingFragment);
                transactionQueue.addTransaction(transaction);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }
}
