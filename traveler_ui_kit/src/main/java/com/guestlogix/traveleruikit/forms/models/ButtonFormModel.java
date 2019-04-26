package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;
import com.guestlogix.traveleruikit.forms.FormFieldType;

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
    public FormFieldType getType() {
        return FormFieldType.BUTTON;
    }
}
