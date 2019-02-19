package com.guestlogix.travelercorekit.validators;

import com.guestlogix.travelercorekit.models.Answer;

public abstract class ValidationRule {
    protected ValidationError error;

    /**
     * Validates the given string.
     *
     * @param toValidate string to validate.
     * @return true if the validation passes.
     */
    public abstract boolean validate(Answer answer);

    public ValidationError getError() {
        return error;
    }
}
