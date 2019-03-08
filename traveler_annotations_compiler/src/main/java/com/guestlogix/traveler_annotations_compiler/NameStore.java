package com.guestlogix.traveler_annotations_compiler;


import com.guestlogix.traveler_annotations.internal.ApiModelSuffix;

public final class NameStore {

    private NameStore() {
        // not to be instantiated in public
    }

    public static String geApiModelClassName(String clsName) {
        return clsName + "$" + clsName + ApiModelSuffix.GENERATED_CLASS_SUFFIX;
    }
}

