package com.guestlogix.travelercorekit.utilities;

public class ObjectMappingException extends RuntimeException {

    Object object;
    Throwable throwable;

    public ObjectMappingException(Object object, Throwable throwable) {
        this.object = object;
        this.throwable = throwable;
    }

    @Override
    public String getMessage() {
        return String.format("Type: %s Cause: %s", object.getClass().getCanonicalName(), throwable.getMessage());
    }
}