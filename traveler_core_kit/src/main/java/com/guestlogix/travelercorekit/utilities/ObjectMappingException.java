package com.guestlogix.travelercorekit.utilities;

import com.guestlogix.travelercorekit.models.ObjectMappingErrorCode;

public class ObjectMappingException extends RuntimeException {

    private ObjectMappingErrorCode code;
    private String type;
    private String field;
    private String error;

    public ObjectMappingException(ObjectMappingErrorCode code, String type, String key, String error) {
        this.code = code;
        this.type = type;
        this.field = key;
        this.error = error;
    }

    @Override
    public String getMessage() {
        switch (code) {
            case MISSING_FIELD:
                return String.format("Missing required field: %s in type: %s. ", field, type);
            case INVALID_FIELD:
                return String.format("Field: %s has invalid value in type: %s. %s", field, type, error);
            case INVALID_DATA:
            default:
                return String.format("Type: %s has invalid data. %s", type, error);
        }
    }
}