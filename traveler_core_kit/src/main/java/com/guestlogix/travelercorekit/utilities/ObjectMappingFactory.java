package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;

import java.io.IOException;

public interface ObjectMappingFactory<T> {
    T instantiate(JsonReader reader) throws ObjectMappingException, IOException;
}