package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public abstract class ValidationRule implements Serializable {
    protected String error;

    /**
     * Validates the given string.
     *
     * @param answer string to validate.
     * @return true if the validation passes.
     */
    public abstract boolean validate(Answer answer);

    public String getErrorMessage() {
        return error;
    }

    static class ValidationRuleObjectMappingFactory implements ObjectMappingFactory<ValidationRule> {
        @Override
        public ValidationRule instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String validationType = jsonObject.getString("validationType");

            if (validationType.toLowerCase().equals("required")) {
                return new RequiredValidationRule();
            } else {
                String validationRule = jsonObject.getString("validationRule");
                String message = jsonObject.getString("message");
                return new PatternValidationRule(validationRule, message);

            }


        }
    }
}
