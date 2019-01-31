package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;

public class Choice {
    private String id;
    private String value;

    public Choice(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public static class ChoiceObjectMappingFactory implements ObjectMappingFactory<Choice> {

        /**
         * Parse the reader object into a Choice without verifying the validity of the contents. If given a reader not
         * of Choice type, resulting Choice object is not guaranteed to have correct values.
         *
         * @param reader Object to parse from.
         * @return Choice model object from the reader.
         * @throws IOException if parsing cannot be completed.
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
