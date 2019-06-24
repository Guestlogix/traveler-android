package com.guestlogix.travelercorekit.utilities;

import java.util.Locale;

public class ObjectMappingError extends Error {
    private Throwable cause;
    private ObjectMappingFactory factory;

    public ObjectMappingError(ObjectMappingFactory factory, Throwable cause) {
        this.factory = factory;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return factory.toString() + ": " + cause.toString();
    }

    @Override
    public String getMessage() {
        return factory.toString() + ": " + cause.toString() + ": " + cause.getMessage();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ObjectMappingError)) {
            return false;
        }

        ObjectMappingError err = (ObjectMappingError) obj;

        return this.cause.equals(err.cause) && this.factory.equals(err.factory);
    }
}
