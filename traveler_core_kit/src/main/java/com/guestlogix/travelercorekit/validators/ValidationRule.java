package com.guestlogix.travelercorekit.validators;

public abstract class ValidationRule {

    /**
     * Validates the given string.
     *
     * @param toValidate string to validate.
     * @return true if the validation passes.
     */
    abstract boolean validate(String toValidate);
}
