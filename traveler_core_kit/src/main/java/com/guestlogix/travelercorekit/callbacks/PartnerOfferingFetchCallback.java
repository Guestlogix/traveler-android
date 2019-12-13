package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;

import java.util.List;

public interface PartnerOfferingFetchCallback {
    /**
     * called when partner offering is fetched successfully
     * @param lstPartnerOfferingGroups is the list of partner offerings group
     */
    void onSuccess(List<PartnerOfferingGroup> lstPartnerOfferingGroups);
    /**
     * called when partner offering could not be fetched because of a problem
     * @param error is the problem details
     */
    void onError(Error error);
}
