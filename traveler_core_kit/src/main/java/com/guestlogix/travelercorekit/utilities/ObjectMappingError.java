package com.guestlogix.travelercorekit.utilities;

import java.util.Locale;

public class ObjectMappingError extends Error {
    private ObjectMappingErrorCode code;
    private Throwable cause;

    public ObjectMappingError(ObjectMappingErrorCode code, Throwable cause) {
        this.code = code;
        this.cause = cause;
    }

    // TODO: Remove this
    public ObjectMappingError(ObjectMappingErrorCode code, String message) {
        this.code = code;
    }

    public ObjectMappingErrorCode getCode() {
        return code;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s %s", code, cause.toString());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectMappingError error = (ObjectMappingError) obj;

        return this.code == error.getCode();
    }
}
