package com.guestlogix.travelercorekit.utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectGLX extends JSONObject {

    public JSONObjectGLX(String json) throws JSONException {
        super(json);
    }

    @Override
    public String getString(String name) throws JSONException {
        if (!has(name) || get(name) == JSONObject.NULL) {
            throw new JSONException("null passed for a non null string");
        } else {
            return super.getString(name);
        }
    }

    public String getNullableString(String name) throws JSONException {
        if (!has(name) || get(name) == JSONObject.NULL) {
            return null;
        } else {
            return super.getString(name);
        }
    }

    public Double getNullableDouble(String name) throws JSONException {
        if (!has(name) || get(name) == JSONObject.NULL) {
            return null;
        } else {
            return super.getDouble(name);
        }
    }

    public Integer getNullableInt(String name) throws JSONException {
        if (!has(name) || get(name) == JSONObject.NULL) {
            return null;
        } else {
            return super.getInt(name);
        }
    }
}
