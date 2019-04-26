package com.guestlogix.traveleruikit.forms;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the types of all form models which are ready to use.
 */
public enum FormFieldType {
    TEXT(0), BUTTON(1), SPINNER(2), QUANTITY(3), DATE(4), HEADER(5), MESSAGE(6);

    private static Map<Integer, FormFieldType> map = new HashMap<>();

    static {
        for (FormFieldType type : FormFieldType.values()) {
            map.put(type.value, type);
        }
    }

    int value;

    FormFieldType(int value) {
        this.value = value;
    }

    public static FormFieldType valueOf(int formType) {
        return map.get(formType);
    }

    public int getValue() {
        return value;
    }
}