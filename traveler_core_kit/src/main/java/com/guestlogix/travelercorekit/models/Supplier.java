package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
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
        public Supplier instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String name = jsonObject.getString("name");

            Trademark trademark = null;
            if (!jsonObject.isNull("trademark"))
                trademark = new Trademark.TrademarkObjectMappingFactory().instantiate(jsonObject.getJSONObject("trademark").toString());

            Assertion.eval(name != null);

            return new Supplier(name, trademark);
        }
    }
}
