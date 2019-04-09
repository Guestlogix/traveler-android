package com.guestlogix.traveleruikit.forms;

public class FormHeader {
    private String title;
    private String subtitle;

    public FormHeader(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
