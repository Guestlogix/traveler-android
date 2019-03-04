package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;
import java.util.List;

public class Catalog {
    private List<CatalogGroup> groups;

    private Catalog(List<CatalogGroup> groups) {
        this.groups = groups;
    }

    public List<CatalogGroup> getGroups() {
        return groups;
    }

    public static class CatalogObjectMappingFactory implements ObjectMappingFactory<Catalog> {

        @Override
        public Catalog instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readGroup(reader);
        }

        private static Catalog readGroup(JsonReader reader) throws IOException, ObjectMappingException {
            List<CatalogGroup> catalogGroups = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                if (name.equals("groups")) {
                    catalogGroups = new ArrayMappingFactory<>(new CatalogGroup.GroupObjectMappingFactory()).instantiate(reader);
                }
            }

            reader.endObject();

            return new Catalog(catalogGroups);
        }
    }
}
