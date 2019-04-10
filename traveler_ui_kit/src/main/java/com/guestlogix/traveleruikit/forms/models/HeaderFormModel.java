package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;

/**
 * Describes a header cell.
 */
public class HeaderFormModel extends FormModel {
    private String title;
    private String subtitle;

    public HeaderFormModel(@Nullable String title, @Nullable String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public int getType() {
        return FormModelType.HEADER.value;
    }
}
