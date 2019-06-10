package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.List;

public class Catalog implements Serializable {
    private List<CatalogGroup> groups;

    private Catalog(List<CatalogGroup> groups) throws IllegalArgumentException {
        if (null == groups) {
            throw new IllegalArgumentException("groups can not be null");
        } else {
            this.groups = groups;
        }
    }

    public List<CatalogGroup> getGroups() {
        return groups;
    }

    /**
     * Factory class to construct Catalog model from {@code JsonReader}.
     */
    static class CatalogObjectMappingFactory implements ObjectMappingFactory<Catalog> {
        /**
         * Parses a reader object into Catalog model.
         *
         * @param reader object to parse from.
         * @return Catalog model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public Catalog instantiate(JsonReader reader) throws Exception {
            List<CatalogGroup> catalogGroups = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                if (key.equals("groups") && JsonToken.NULL != reader.peek()) {
                    catalogGroups = new ArrayMappingFactory<>(new CatalogGroup.GroupObjectMappingFactory()).instantiate(reader);
                } else {
                    catalogGroups = null;
                    reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != catalogGroups, "groups can not be null");

            return new Catalog(catalogGroups);
        }
    }
}
