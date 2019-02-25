package com.guestlogix.travelercorekit.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public interface Payment extends Serializable {
    List<Attribute> getAttributes();
    JSONObject getSecurePayload();
}
