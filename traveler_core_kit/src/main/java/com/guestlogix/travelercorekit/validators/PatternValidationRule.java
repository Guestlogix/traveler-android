package com.guestlogix.travelercorekit.validators;

import com.guestlogix.travelercorekit.models.Answer;

import java.util.regex.Pattern;

public class PatternValidationRule extends ValidationRule {

    private final Pattern REGEX;

    /**
     * Initializes the pattern validator with the given pattern.
     * Does not check the validity of the give pattern.
     *
     * @param pattern that will be used for all validations.
     */
    public PatternValidationRule(String pattern) {
        REGEX = Pattern.compile(pattern);
    }

    /**
     * Validates the given string based on the pattern.
     * @param answer To validate
     * @return true if regex validation passes.
     */
    @Override
    boolean validate(Answer answer) {
        boolean isValid = answer != null && REGEX.matcher(answer.getCodedValue()).find();

        if (!isValid) {
            error = ValidationError.REGEX_MISMATCH;
        }

        return isValid;
    }
}
