package com.guestlogix.traveleruikit.forms;

/**
 * Class used to describe the contents of a section header.
 * <p>
 * Creates an {@link com.guestlogix.traveleruikit.forms.models.HeaderFormModel} in the background.
 */
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
