package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;


public class Pass implements ProductOffering, Serializable {
    private String id;
    private String name;
    private String description;
    private Price price;

    private Pass(@NonNull String id, @NonNull String name, String description, @NonNull Price price) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    static class PassObjectMappingFactory implements ObjectMappingFactory<Pass> {
        @Override
        public Pass instantiate(JsonReader reader) throws Exception {
            String id = null;
            String name = null;
            String description = null;
            Price price = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "title":
                        name = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "price":
                        ObjectMappingFactory<Price> p = new Price.PriceObjectMappingFactory();
                        price = p.instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(name != null);
            Assertion.eval(price != null);

            return new Pass(id, name, description, price);
        }
    }
}