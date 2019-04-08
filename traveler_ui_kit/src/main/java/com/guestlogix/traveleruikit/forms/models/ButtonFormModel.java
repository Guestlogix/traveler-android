package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;

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
    int getType() {
        return FormModelType.BUTTON.value;
    }
}
