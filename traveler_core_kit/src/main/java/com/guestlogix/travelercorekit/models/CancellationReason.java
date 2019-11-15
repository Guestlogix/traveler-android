package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;

public class CancellationReason implements Serializable {
    private String id;
    private String value;
    private boolean explanationRequired;

    CancellationReason(@NonNull String id, @NonNull String value, @NonNull boolean explanationRequired) {
        this.id = id;
        this.value = value;
        this.explanationRequired = explanationRequired;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public boolean isExplanationRequired() {
        return explanationRequired;
    }

    static class CancellationReasonsObjectMappingFactory implements ObjectMappingFactory<CancellationReason> {
        @Override
        public CancellationReason instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String value = jsonObject.getString("value");
            boolean explanationRequired = jsonObject.getBoolean("explanationRequired");

            Assertion.eval(id != null);
            Assertion.eval(value != null);

            return new CancellationReason(id, value, explanationRequired);
        }
    }
}
