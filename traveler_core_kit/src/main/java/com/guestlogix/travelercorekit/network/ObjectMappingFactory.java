package com.guestlogix.travelercorekit.network;

import android.util.JsonReader;

import java.io.IOException;

//ObjectMappingFactory
public interface ObjectMappingFactory<T> {
    T instantiate(JsonReader reader) throws ObjectMappingException, IOException;
}