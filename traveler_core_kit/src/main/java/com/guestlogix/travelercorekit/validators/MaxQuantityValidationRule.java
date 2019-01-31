package com.guestlogix.travelercorekit.validators;

public class MaxQuantityValidationRule extends ValidationRule {
    private final int maxQuantity;

    public MaxQuantityValidationRule(int max) {
        this.maxQuantity = max;
    }

    /**
     * Validates whether the given string is of type int and is less than or equal to max amount.
     *
     * @param toValidate string to validate.
     * @return true if the validation passes.
     */
    @Override
    boolean validate(String toValidate) {
        try {
            int value = Integer.parseInt(toValidate);

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
        return toValidate <= maxQuantity;
    }
}
