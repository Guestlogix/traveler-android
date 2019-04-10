package com.guestlogix.traveleruikit.forms.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Predefined form types.
 * For internal use mostly.
 */
public enum FormModelType {
    TEXT(0), BUTTON(1), SPINNER(2), QUANTITY(3), DATE(4), HEADER(5), MESSAGE(6);

    private static Map<Integer, FormModelType> map = new HashMap<>();

    static {
        for (FormModelType type : FormModelType.values()) {
            map.put(type.value, type);
        }
    }

    int value;

    FormModelType(int value) {
        this.value = value;
    }

    public static FormModelType valueOf(int formType) {
        return map.get(formType);
    }
}