package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

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

    static class CatalogObjectMappingFactory implements ObjectMappingFactory<Catalog> {
        @Override
        public Catalog instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            List<CatalogGroup> catalogGroups = new CatalogGroupListMappingFactory().instantiate(jsonObject.getJSONArray("groups").toString());

            Assertion.eval(catalogGroups != null);

            return new Catalog(catalogGroups);
        }

    }
}
