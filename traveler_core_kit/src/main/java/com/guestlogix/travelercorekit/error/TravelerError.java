package com.guestlogix.travelercorekit.error;

import java.util.Locale;

public class TravelerError {
    private TravelerErrorCode mCode;
    private String mMessage;

    public TravelerError(TravelerErrorCode code, String message) {
        mCode = code;
        mMessage = message;
    }

    public TravelerErrorCode getCode() {
        return mCode;
    }

    public String toString() {
        return String.format(Locale.CANADA, "%s %s", mCode , mMessage);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TravelerError error = (TravelerError) obj;

        return this.mCode == error.getCode();
    }
}
