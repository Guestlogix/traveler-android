package com.guestlogix.traveleruikit.forms.models;

import com.guestlogix.traveleruikit.forms.FormMessage;
import com.guestlogix.traveleruikit.forms.FormFieldType;

/**
 * Describes a message cell.
 */
public class MessageFormModel extends FormModel {
    private FormMessage formMessage;

    public MessageFormModel(FormMessage formMessage) {
        this.formMessage = formMessage;
    }

    public FormMessage getFormMessage() {
        return formMessage;
    }

    @Override
    public FormFieldType getType() {
        return FormFieldType.MESSAGE;
    }
}
