package com.guestlogix.travelercorekit.validators;

import com.guestlogix.travelercorekit.models.Answer;

public class MaxQuantityValidationRule extends ValidationRule {
    private final int maxQuantity;

    public MaxQuantityValidationRule(int max) {
        this.maxQuantity = max;
    }

    /**
     * Validates whether the given string is of type int and is less than or equal to max amount.
     *
     * @param answer to validate.
     * @return true if the validation passes.
     */
    @Override
    boolean validate(Answer answer) {
        try {
            int value = Integer.parseInt(answer.getCodedValue());

            return validate(value);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valdiates whether the given integer is less than or equal to max amount.
     *
     * @param toValidate string to validate.
     * @return true if the validation passes
     */
    public boolean validate(int toValidate) {
        boolean isValid = toValidate <= maxQuantity;

        if (!isValid) {
            error = ValidationError.BAD_QUANTITY;
        }

        return isValid;
    }
}
