package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;

public class Attribute {

    private String label;
    private String value;

    public Attribute(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static class AttributeObjectMappingFactory implements ObjectMappingFactory<Attribute> {

        @Override
        public Attribute instantiate(JsonReader reader) throws IOException {
            return readItem(reader);
        }

        private Attribute readItem(JsonReader reader) throws IOException {
            String label = "";
            String value = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "label":
                        label = JsonReaderHelper.readString(reader);
                        break;
                    case "value":
                        value = JsonReaderHelper.readString(reader);
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            return new Attribute(label, value);
        }
    }
}
