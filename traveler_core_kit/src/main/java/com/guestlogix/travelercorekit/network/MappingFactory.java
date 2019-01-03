package com.guestlogix.travelercorekit.network;

import org.json.JSONObject;

//MappingFactory
public interface MappingFactory<T> {

    T instantiate(JSONObject jsonObject) throws MappingException;
}