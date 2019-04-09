package com.guestlogix.traveleruikit.forms;

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
