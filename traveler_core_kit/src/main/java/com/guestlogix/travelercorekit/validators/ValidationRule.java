package com.guestlogix.travelercorekit.validators;

public abstract class ValidationRule {

    /**
     * Validates the given string.
     *
     * @param toValidate string to validate.
     * @return whether the string is valid according to the rules.
     */
    abstract boolean validate(String toValidate);
}
