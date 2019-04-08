package com.guestlogix.traveleruikit.forms.models;

public abstract class FormModel {

    abstract int getType();

    enum FormModelType {
        TEXT(0), BUTTON(1), SPINNER(2), QUANTITY(2), DATE(3), HEADER(4);

        int value;

        FormModelType(int value) {
            this.value = value;
        }
    }
}
