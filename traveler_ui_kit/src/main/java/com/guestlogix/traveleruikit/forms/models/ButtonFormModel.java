package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;

/**
 * Describes a button form cell.
 */
public class ButtonFormModel extends FormModel {
    private String text;

    public ButtonFormModel(@Nullable String text) {
        this.text = text;
    }

    @Nullable
    public String getText() {
        return text;
    }

    @Override
    public int getType() {
        return FormModelType.BUTTON.value;
    }
}
