package com.guestlogix.travelercorekit.utilities;

public interface ObjectMappingFactory<T> {
    T instantiate(String rawResponse) throws Exception;
}