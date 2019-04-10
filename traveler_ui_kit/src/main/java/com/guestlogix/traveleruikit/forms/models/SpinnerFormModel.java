package com.guestlogix.traveleruikit.forms.models;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Describes a spinner cell
 */
public class SpinnerFormModel extends FormModel {
    private String hint;
    private List<String> options;

    public SpinnerFormModel(@Nullable String hint, @Nullable List<String> options) {
        this.hint = hint;
        this.options = options;
    }

    @Nullable
    public String getHint() {
        return hint;
    }

    @Nullable
    public List<String> getOptions() {
        return options;
    }

    @Override
    public int getType() {
        return FormModelType.SPINNER.value;
    }
}
