package com.guestlogix.travelercorekit.error;

import java.util.Locale;

public class TravelerError {
    private TravelerErrorCode code;
    private String message;

    public TravelerError(TravelerErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public TravelerErrorCode getCode() {
        return code;
    }

    public String toString() {
        return String.format(Locale.CANADA, "%s %s", code, message);
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

        return this.code == error.getCode();
    }
}
