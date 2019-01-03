package com.guestlogix.travelercorekit.models;

import com.guestlogix.task.MappingException;
import com.guestlogix.task.MappingFactory;
import org.json.JSONObject;

import java.util.ArrayList;

public class Catalog {

    ArrayList<Group> mGroups;

    public Catalog(JSONObject jsonObject) throws MappingException {

//        try {
//            this.mGroups = jsonObject.getString("token");
//        } catch (JSONException e) {
//            throw new MappingException();
//        }
    }

    public ArrayList<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.mGroups = groups;
    }

    public static class CatalogMappingFactory implements MappingFactory<Catalog> {
        @Override
        public Catalog instantiate(JSONObject jsonObject) throws MappingException {
            return new Catalog(jsonObject);
        }
    }
}
