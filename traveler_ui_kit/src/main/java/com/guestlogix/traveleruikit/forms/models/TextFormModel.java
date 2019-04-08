package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;

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
    int getType() {
        return FormModelType.TEXT.value;
    }
}
