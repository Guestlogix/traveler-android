package com.guestlogix.traveleruikit.forms.utilities;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum FormType {
    HEADER(0), TEXT(1), BUTTON(2), QUANTITY(3), SPINNER(4);

    private int value;
    @SuppressLint("UseSparseArrays")
    private static Map<Integer, FormType> map = new HashMap<>();


    FormType(int value) {
        this.value = value;
    }

    static {
        for (FormType type : FormType.values()) {
            map.put(type.value, type);
        }
    }

    public static FormType valueOf(int formType) {
        return map.get(formType);
    }

    public int getValue() {
        return value;
    }

    public static int getTypeCount() {
        return map.size();
    }
}
