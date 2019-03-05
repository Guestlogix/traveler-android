package com.guestlogix.travelercorekit.models;

import java.io.Serializable;

public abstract class ValidationRule implements Serializable {
    protected ValidationError error;

    /**
     * Validates the given string.
     *
     * @param answer string to validate.
     * @return true if the validation passes.
     */
    public abstract boolean validate(Answer answer);

    public ValidationError getError() {
        return error;
    }
}
