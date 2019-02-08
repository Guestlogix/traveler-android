package com.guestlogix.traveleruikit.forms.listeners;

import com.guestlogix.traveleruikit.forms.models.BaseElement;

/**
 * Listener for value change in the elements.
 */
public interface OnFormElementValueChangedListener {
    /**
     * Is called whenever the value was changed inside an element.
     *
     * @param e Element where the value was changed.
     */
    void onValueChanged(BaseElement e);
}
