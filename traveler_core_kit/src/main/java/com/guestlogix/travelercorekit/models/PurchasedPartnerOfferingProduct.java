package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.util.Date;
import java.util.List;

public class PurchasedPartnerOfferingProduct implements Product {
    private String id;
    private String title;
    private Price price;
    private List<PartnerOffering> partnerOfferings;
    private ProductType productType = ProductType.PARTNER_OFFERING;
    private String cancellationPolicy;

    PurchasedPartnerOfferingProduct(@NonNull String id,
                                    String title,
                                    @NonNull Price price,
                                    @NonNull List<PartnerOffering> partnerOfferings, String cancellationPolicy) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.partnerOfferings = partnerOfferings;
        this.cancellationPolicy = cancellationPolicy;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Override
    public Price getPrice() {
        return price;
    }

    public List<PartnerOffering> getPartnerOfferings() {
        return partnerOfferings;
    }

    @Override
    public ProductType getProductType() {
        return productType;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    static class PartnerOfferingPurchasedProductObjectMappingFactory implements ObjectMappingFactory<PurchasedPartnerOfferingProduct> {
        @Override
        public PurchasedPartnerOfferingProduct instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String id = jsonObject.getString("id");
            String title = jsonObject.getNullableString( "title");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("price").toString());
            List<Pass> passes = new ArrayMappingFactory<>(new Pass.PassObjectMappingFactory()).instantiate(jsonObject.getJSONArray("passes").toString());
            Date eventDate = DateHelper.parseISO8601(jsonObject.getString("experienceDate"));

            String cancellationPolicy = jsonObject.getString("cancellationPolicy");

            Assertion.eval(id != null);
            Assertion.eval(title != null);
            Assertion.eval(price != null);
            Assertion.eval(eventDate != null);

            //TODO: fix pass / partner offering casting conflict
            return new PurchasedPartnerOfferingProduct(id, title, price, (List<PartnerOffering>) (List<?>) passes, cancellationPolicy);
        }
    }
}
