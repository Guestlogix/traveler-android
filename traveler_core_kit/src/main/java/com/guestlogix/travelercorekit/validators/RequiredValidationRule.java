package com.guestlogix.travelercorekit.validators;

import android.text.TextUtils;

public class RequiredValidationRule extends ValidationRule {

    /**
     * Verifies whether the string to validate is present.
     *
     * @param toValidate String to validate.
     * @return True if validation passes.
     */
    @Override
    boolean validate(String toValidate) {
        return !TextUtils.isEmpty(toValidate);
    }
}
