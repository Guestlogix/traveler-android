package com.guestlogix.travelercorekit.validators;

import android.text.TextUtils;
import com.guestlogix.travelercorekit.models.Answer;

public class RequiredValidationRule extends ValidationRule {

    /**
     * Verifies whether the string to validate is present.
     *
     * @param answer to validate.
     * @return True if validation passes.
     */
    @Override
    boolean validate(Answer answer) {
        boolean isValid = answer != null && !TextUtils.isEmpty(answer.getCodedValue());

        if (!isValid) {
            error = ValidationError.REQUIRED;
        }

        return isValid;
    }
}
