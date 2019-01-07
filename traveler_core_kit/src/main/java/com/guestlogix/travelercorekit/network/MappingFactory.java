package com.guestlogix.travelercorekit.network;

import android.util.JsonReader;

import java.io.IOException;

//MappingFactory
public interface MappingFactory<T> {

    T instantiate(JsonReader reader) throws MappingException, IOException;
}