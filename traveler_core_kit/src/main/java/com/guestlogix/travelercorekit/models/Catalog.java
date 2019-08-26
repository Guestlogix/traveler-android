package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class Catalog implements Serializable {
    private List<CatalogGroup> groups;

    private Catalog(@NonNull List<CatalogGroup> groups) {
        this.groups = groups;
    }

    public List<CatalogGroup> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "groups=" + groups +
                '}';
    }

    static class CatalogObjectMappingFactory implements ObjectMappingFactory<Catalog> {
        @Override
        public Catalog instantiate(JsonReader reader) throws Exception {
            List<CatalogGroup> catalogGroups = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                if (key.equals("groups")) {
                    catalogGroups = new ArrayMappingFactory<>(new CatalogGroup.GroupObjectMappingFactory()).instantiate(reader);
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(catalogGroups != null);

            return new Catalog(catalogGroups);
        }

    }
}
