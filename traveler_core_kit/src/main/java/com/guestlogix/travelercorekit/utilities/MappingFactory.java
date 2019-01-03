package com.guestlogix.travelercorekit.utilities;

import com.guestlogix.travelercorekit.utilities.MappingException;
import org.json.JSONObject;

//MappingFactory
public interface MappingFactory<T> {

    T instantiate(JSONObject jsonObject) throws MappingException;
}