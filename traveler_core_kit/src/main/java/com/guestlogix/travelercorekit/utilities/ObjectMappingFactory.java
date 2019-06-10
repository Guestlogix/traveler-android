package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public interface ObjectMappingFactory<T> {
    T instantiate(JsonReader reader) throws Exception;
}