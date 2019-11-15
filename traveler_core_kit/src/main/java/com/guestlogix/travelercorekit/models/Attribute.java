package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;

public class Attribute implements Serializable {
    private String label;
    private String value;

    public Attribute(@NonNull String label, @NonNull String value) throws IllegalArgumentException {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    static class AttributeObjectMappingFactory implements ObjectMappingFactory<Attribute> {
        @Override
        public Attribute instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String label = jsonObject.getString("label");
            String value = jsonObject.getString("value");

            Assertion.eval(label != null);
            Assertion.eval(value != null);

            return new Attribute(label, value);
        }

    }
}
