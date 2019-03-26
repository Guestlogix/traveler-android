package com.guestlogix.traveleruikit.forms.descriptors;

import java.util.Date;

public class DateDescriptor extends InputDescriptor {
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
     * Default date in the input field.
     * Leave null if there is no value yet.
     */
    public Date defaultDate;

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
