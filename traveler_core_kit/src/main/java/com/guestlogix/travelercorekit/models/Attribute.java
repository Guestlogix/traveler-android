package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
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
        public Attribute instantiate(JsonReader reader) throws Exception {
            String label = null;
            String value = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "label":
                        label = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "value":
                        value = JsonReaderHelper.nextNullableString(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(label != null);
            Assertion.eval(value != null);

            return new Attribute(label, value);
        }

    }
}
