package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;
import com.guestlogix.traveleruikit.forms.FormFieldType;

/**
 * Describes a quantity cell.
 */
public class QuantityFormModel extends FormModel {
    private String title;
    private String subtitle;
    private int maxValue;
    private int minValue;

    public QuantityFormModel(@Nullable String title, @Nullable String subtitle, @Nullable int maxValue, @Nullable int minValue) {
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
    public int getMaxValue() {
        return maxValue;
    }

    @Nullable
    public int getMinValue() {
        return minValue;
    }

    @Override
    public FormFieldType getType() {
        return FormFieldType.QUANTITY;
    }
}
