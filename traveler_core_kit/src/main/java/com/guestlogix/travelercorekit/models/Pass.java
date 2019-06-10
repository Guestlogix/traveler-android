package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;


public class Pass implements Serializable {
    private String id;
    private String name;
    private String description;
    private Price price;

    private Pass(String id, String name, String description, Price price) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be empty");
        } else {
            this.id = id;
        }
        if (price == null) {
            throw new IllegalArgumentException("price can not be empty");
        } else {
            this.price = price;
        }
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    /**
     * Factory class to construct Pass model from {@code JsonReader}.
     */
    static class PassObjectMappingFactory implements ObjectMappingFactory<Pass> {

        /**
         * Parses a json reader object into a Pass model.
         *
         * @param reader to read from.
         * @return Pass model.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public Pass instantiate(JsonReader reader) throws Exception {
            String id = "";
            String name = "";
            String description = "";
            Price price = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "title":
                        name = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "price":
                        if (JsonToken.NULL != reader.peek()) {
                            price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        } else {
                            price = null;
                            reader.skipValue();
                        }
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(null != id && !id.isEmpty(), "id can not be empty");
            Assertion.eval(null != price, "price can not be empty");

            return new Pass(id, name, description, price);
        }
    }
}