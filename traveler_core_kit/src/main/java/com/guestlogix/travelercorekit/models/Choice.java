package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;

public class Choice implements Serializable {
    private String id;
    private String value;

    Choice(@NonNull String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    static class ChoiceObjectMappingFactory implements ObjectMappingFactory<Choice> {
        @Override
        public Choice instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String id = jsonObject.getString("id");
            String value = jsonObject.getNullableString("label");

            Assertion.eval(id != null);

            return new Choice(id, value);
        }
    }
}
