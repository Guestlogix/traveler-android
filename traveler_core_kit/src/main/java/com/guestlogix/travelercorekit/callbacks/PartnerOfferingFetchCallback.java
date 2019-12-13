package com.guestlogix.travelercorekit.callbacks;

import com.guestlogix.travelercorekit.models.PartnerOfferingGroup;

import java.util.List;

public interface PartnerOfferingFetchCallback {
    void onSuccess(List<PartnerOfferingGroup> lstPartnerOfferingGroups);
    void onError(Error error);
}
