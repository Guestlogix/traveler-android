package com.guestlogix.travelercorekit.models;

import java.util.regex.Pattern;

public class PatternValidationRule extends ValidationRule {

    private final Pattern regex;

    /**
     * Initializes the pattern validator with the given pattern.
     * Does not check the validity of the give pattern.
     *
     * @param pattern that will be used for all validations.
     */
    PatternValidationRule(String pattern, String error) {
        this.regex = Pattern.compile(pattern);
        this.error = error;
    }

    /**
     * Validates the given string based on the pattern.
     *
     * @param answer To validate
     * @return true if regex validation passes.
     */
    @Override
    public boolean validate(Answer answer) {
        boolean isValid = answer != null && regex.matcher(answer.getCodedValue()).find();

        return isValid;
    }
}
