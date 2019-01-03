package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.MappingException;
import com.guestlogix.travelercorekit.utilities.MappingFactory;
import org.json.JSONObject;

import java.util.ArrayList;

public class Catalog {

    ArrayList<Group> groups;

    public Catalog(JSONObject jsonObject) throws MappingException {

//        try {
//            this.groups = jsonObject.getString("token");
//        } catch (JSONException e) {
//            throw new MappingException();
//        }
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public static class CatalogMappingFactory implements MappingFactory<Catalog> {
        @Override
        public Catalog instantiate(JSONObject jsonObject) throws MappingException {
            return new Catalog(jsonObject);
        }
    }
}
