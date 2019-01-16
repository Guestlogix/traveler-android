package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;

import java.io.IOException;
import java.util.List;

public class Catalog {
    private List<Group> groups;

    public Catalog(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public static class CatalogObjectMappingFactory implements ObjectMappingFactory<Catalog> {

        @Override
        public Catalog instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            return readGroup(reader);
        }

        private static Catalog readGroup(JsonReader reader) throws IOException, ObjectMappingException {
            List<Group> groups = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                if (name.equals("groups")) {
                    groups = new ArrayMappingFactory<>(new Group.GroupObjectMappingFactory()).instantiate(reader);
                }
            }

            reader.endObject();

            return new Catalog(groups);
        }
    }
}
