package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;
import com.guestlogix.traveleruikit.forms.FormFieldType;

/**
 * Describes a text input cell.
 */
public class TextFormModel extends FormModel {
    private String hint;

    public TextFormModel(@Nullable String hint) {
        this.hint = hint;
    }

    @Nullable
    public String getHint() {
        return hint;
    }

    @Override
    public FormFieldType getType() {
        return FormFieldType.TEXT;
    }
}
