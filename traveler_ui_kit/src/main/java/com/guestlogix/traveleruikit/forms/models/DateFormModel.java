package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;

import java.util.Date;

/**
 * Describes a date cell.
 */
public class DateFormModel extends FormModel {
    private String hint;
    private Date minDate;
    private Date maxDate;

    public DateFormModel(@Nullable String hint, @Nullable Date minDate, @Nullable Date maxDate) {
        this.hint = hint;
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    @Nullable
    public String getHint() {
        return hint;
    }

    @Nullable
    public Date getMinDate() {
        return minDate;
    }

    @Nullable
    public Date getMaxDate() {
        return maxDate;
    }

    @Override
    public int getType() {
        return FormModelType.DATE.value;
    }
}
