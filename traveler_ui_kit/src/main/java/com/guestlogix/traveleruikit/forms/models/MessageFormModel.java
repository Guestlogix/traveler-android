package com.guestlogix.traveleruikit.forms.models;

import com.guestlogix.traveleruikit.forms.FormMessage;

public class MessageFormModel extends FormModel {
    private FormMessage formMessage;

    public MessageFormModel(FormMessage formMessage) {
        this.formMessage = formMessage;
    }

    public FormMessage getFormMessage() {
        return formMessage;
    }

    @Override
    public int getType() {
        return FormModelType.MESSAGE.value;
    }
}
