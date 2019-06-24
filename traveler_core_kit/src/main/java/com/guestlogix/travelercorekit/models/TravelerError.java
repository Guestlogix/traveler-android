package com.guestlogix.travelercorekit.models;

import java.util.Locale;

// TODO: Remove this class entirely
public class TravelerError extends Error {
    private TravelerErrorCode code;

    public TravelerError(TravelerErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public TravelerErrorCode getCode() {
        return code;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%s %s", code, this.getMessage());
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
