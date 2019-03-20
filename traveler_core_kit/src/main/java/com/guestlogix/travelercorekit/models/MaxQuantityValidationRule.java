package com.guestlogix.travelercorekit.models;

public class MaxQuantityValidationRule extends ValidationRule {
    private final int maxQuantity;

    MaxQuantityValidationRule(int max) {
        this.maxQuantity = max;
    }

    /**
     * Validates whether the given string is of type int and is less than or equal to max amount.
     *
     * @param answer to validate.
     * @return true if the validation passes.
     */
    @Override
    public boolean validate(Answer answer) {
        try {
            int value = Integer.parseInt(answer.getCodedValue());

            boolean isValid = value <= maxQuantity;

            if (!isValid) {
                error = ValidationError.BAD_QUANTITY;
            }

            return isValid;
        } catch (Exception e) {
            error = ValidationError.BAD_QUANTITY;
            return false;
        }
    }
}
