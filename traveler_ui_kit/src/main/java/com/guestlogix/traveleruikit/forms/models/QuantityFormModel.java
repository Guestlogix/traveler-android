package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;

/**
 * Describes a quantity cell.
 */
public class QuantityFormModel extends FormModel {
    private String title;
    private String subtitle;
    private Integer maxValue;
    private Integer minValue;

    public QuantityFormModel(@Nullable String title, @Nullable String subtitle, @Nullable Integer maxValue, @Nullable Integer minValue) {
        this.title = title;
        this.subtitle = subtitle;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    @Nullable
    public Integer getMaxValue() {
        return maxValue;
    }

    @Nullable
    public Integer getMinValue() {
        return minValue;
    }

    @Override
    public int getType() {
        return FormModelType.QUANTITY.value;
    }
}
