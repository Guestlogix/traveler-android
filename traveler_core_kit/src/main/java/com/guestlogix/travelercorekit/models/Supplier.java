package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class Supplier implements Serializable {
    private String name;
    private Trademark trademark;

    Supplier(@NonNull String name, @Nullable Trademark trademark) {
        this.name = name;
        this.trademark = trademark;
    }

    public String getName() {
        return name;
    }

    public Trademark getTrademark() {
        return trademark;
    }

    public static class SupplierObjectMappingFactory implements ObjectMappingFactory<Supplier> {
        @Override
        public Supplier instantiate(JsonReader reader) throws Exception {
            String name = null;
            Trademark trademark = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                if (key.equals("name")) {
                    name = reader.nextString();
                } else if (key.equals("trademark")) {
                    if (reader.peek() != JsonToken.NULL)
                        trademark = new Trademark.TrademarkObjectMappingFactory().instantiate(reader);
                    else
                        reader.skipValue();
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(name != null);

            return new Supplier(name, trademark);
        }
    }
}
