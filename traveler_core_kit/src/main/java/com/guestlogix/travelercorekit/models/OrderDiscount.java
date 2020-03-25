package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public final class OrderDiscount implements Serializable {
    private String discountToken;
    private String label;
    private String promoCode;
    private Status status;
    private String termsAndConditionsLink;
    private Price amount;

    OrderDiscount(@NonNull String discountToken,
                  @Nullable String label,
                  @Nullable String promoCode,
                  @NonNull Status status,
                  @Nullable String termsAndConditionsLink,
                  @NonNull Price amount) {
        this.discountToken = discountToken;
        this.label = label;
        this.promoCode = promoCode;
        this.status = status;
        this.termsAndConditionsLink = termsAndConditionsLink;
        this.amount = amount;
    }

    //region ===================== Discount status ======================

    public enum Status {
        PENDING, APPLIED, UNKNOWN;

        public static Status fromString(String value) {
            switch (value) {
                case "Pending":
                    return PENDING;
                case "Applied":
                    return APPLIED;
                default:
                    return UNKNOWN;
            }
        }
    }

    //endregion

    public String getDiscountToken() {
        return discountToken;
    }

    public String getLabel() {
        return label;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public Status getStatus() {
        return status;
    }

    public String getTermsAndConditionsLink() {
        return termsAndConditionsLink;
    }

    public Price getAmount() {
        return amount;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof OrderDiscount && ((OrderDiscount) obj).discountToken.equals(this.discountToken));
    }

    static class OrderDiscountMappingFactory implements ObjectMappingFactory<OrderDiscount> {
        @Override
        public OrderDiscount instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String discountToken = jsonObject.getString("discountToken");
            String label = jsonObject.getNullableString("label");
            String promoCode = jsonObject.getNullableString("promoCode");
            String termsAndConditions = jsonObject.getNullableString("termsAndConditions");

            Price amount = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("amount").toString());

            Status status = Status.fromString(jsonObject.getString("status"));

            return new OrderDiscount(discountToken, label, promoCode, status, termsAndConditions, amount);
        }
    }
}
