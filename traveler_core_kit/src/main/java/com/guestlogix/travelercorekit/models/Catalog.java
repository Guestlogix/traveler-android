package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.util.List;

public class Catalog {
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
         * @param reader Object to parse from.
         * @return Catalog model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public Catalog instantiate(JsonReader reader) throws ObjectMappingException {
            try {
                List<CatalogGroup> catalogGroups = null;

                reader.beginObject();

                while (reader.hasNext()) {
                    String name = reader.nextName();

                    if (name.equals("groups")) {
                        catalogGroups = new ArrayMappingFactory<>(new CatalogGroup.GroupObjectMappingFactory()).instantiate(reader);
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
                return new Catalog(catalogGroups);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, e.getMessage()));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }

    }
}
