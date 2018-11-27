package com.guestlogix.network;

import org.json.JSONObject;

public interface Mappable {
    Mappable instantiate(JSONObject json);

}
