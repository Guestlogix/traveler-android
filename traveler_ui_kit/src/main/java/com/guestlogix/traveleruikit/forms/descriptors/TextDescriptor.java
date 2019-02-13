package com.guestlogix.traveleruikit.forms.descriptors;

public class TextDescriptor extends InputDescriptor {
    /**
     * Hint to be displayed on the input field.
     */
    public String hint;

    /**
     * Current value in the input field.
     * Leave null if there is no value yet.
     */
    public String value;

    /**
     * Error message to be displayed for the input.
     * Leave null if no error should be displayed.
     */
    public String error;

    /**
     * Info message to be displayed for the input.
     * Leave null if no info message should be displayed.
     */
    public String info;
}
