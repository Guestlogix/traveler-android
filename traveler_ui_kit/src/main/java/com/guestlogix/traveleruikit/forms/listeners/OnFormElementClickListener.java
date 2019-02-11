package com.guestlogix.traveleruikit.forms.listeners;

import com.guestlogix.traveleruikit.forms.models.BaseElement;

/**
 * Listener for click events in the elements of the form.
 */
public interface OnFormElementClickListener {
    /**
     * Is invoked whenever a user performs a click on an element of the form.
     *
     * @param element Where the click was performed.
     */
    void onFormElementClick(BaseElement element);
}
