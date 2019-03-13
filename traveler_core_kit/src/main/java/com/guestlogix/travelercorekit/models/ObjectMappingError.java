package com.guestlogix.travelercorekit.models;

import java.util.Locale;

public class ObjectMappingError extends Error {
    private ObjectMappingErrorCode code;
    private String message;

    public ObjectMappingError(ObjectMappingErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public ObjectMappingErrorCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%s %s", code, message);
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