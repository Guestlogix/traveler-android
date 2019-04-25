package com.guestlogix.traveleruikit.forms;

/**
 * A class used to describe a message in the form. The message can be either an info message or an alert.
 * <p>
 * Alert messages are displayed as errors in the form.
 */
public class FormMessage {
    private String message;
    private FormMessageType type;

    public FormMessage(String message, FormMessageType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public FormMessageType getType() {
        return type;
    }

    public enum FormMessageType {
        INFO, ALERT
    }
}
