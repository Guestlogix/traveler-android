package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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
        public CancellationReason instantiate(JsonReader reader) throws Exception {
            String id = null;
            String value = null;
            boolean explanationRequired = false;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "value":
                        value = reader.nextString();
                        break;
                    case "explanationRequired":
                        explanationRequired = reader.nextBoolean();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(value != null);

            return new CancellationReason(id, value, explanationRequired);
        }
    }
}
