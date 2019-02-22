package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.io.Serializable;

public class Choice implements Serializable {
    private String id;
    private String value;

    public Choice(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static class ChoiceObjectMappingFactory implements ObjectMappingFactory<Choice> {

        /**
         * Parses a reader object into Choice model. Does not guarantee the correctness of the resulting object.
         *
         * @param reader Object to parse from.
         * @return Choice model object from the reader.
         * @throws IOException if mapping fails.
         */
        @Override
        public Choice instantiate(JsonReader reader) throws IOException {
            return readChoice(reader);
        }

        private Choice readChoice(JsonReader reader) throws IOException {
            String id = "";
            String value = "";

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "id":
                        id = JsonReaderHelper.readString(reader);
                        break;
                    case "name":
                        value = JsonReaderHelper.readString(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            return new Choice(id, value);
        }
    }
}
