package com.guestlogix.travelercorekit.models;

import android.text.TextUtils;

public class RequiredValidationRule extends ValidationRule {
    /**
     * Verifies whether the string to validate is present.
     *
     * @param answer to validate.
     * @return True if validation passes.
     */
    @Override
    public boolean validate(Answer answer) {
        boolean isValid = answer != null && !TextUtils.isEmpty(answer.getCodedValue());

        return isValid;
    }
}
