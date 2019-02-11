package com.guestlogix.traveleruikit.forms.listeners;

import com.guestlogix.traveleruikit.forms.models.BaseElement;

public interface OnFormElementFocusChangedListener {
    void onFocusChanged(BaseElement element, boolean hasFocus);
}
