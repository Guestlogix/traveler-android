package com.guestlogix.travelercorekit.tasks;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.models.ObjectMappingError;
import com.guestlogix.travelercorekit.models.ObjectMappingErrorCode;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.IOException;

public class NetworkTaskError extends Error {

    /**
     * Factory class to construct NetworkTaskError model from {@code JsonReader}.
     */
    static public class NetworkTaskErrorMappingFactory implements ObjectMappingFactory<NetworkTaskError> {
        /**
         * Parses a reader object into Availability model.
         *
         * @param reader Object to parse from.
         * @return Availability model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public NetworkTaskError instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "NetworkTaskError";
            try {
                Integer errorCode = null;
                String errorMessage = null;
                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "errorCode":
                            errorCode = JsonReaderHelper.readInteger(reader);
                            break;
                        case "errorMessage":
                            errorMessage = JsonReaderHelper.readString(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new NetworkTaskError(Code.SERVER_ERROR, errorMessage);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }

    public enum Code {
        BAD_URL, CONNECTION_ERROR, NO_REQUEST, FORBIDDEN, UNAUTHORIZED, SERVER_ERROR
    }

    private Code code;

    NetworkTaskError(Code code) {
        super();
        this.code = code;
    }

    NetworkTaskError(Code code, String message) {
        super(message);

        this.code = code;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getCodeValue(), super.toString());
    }

    @Override
    public String getMessage() {
        return String.format("%s", super.getMessage());
    }

    public Code getCode() {
        return code;
    }

    private String getCodeValue() {

        switch (code) {
            case BAD_URL:
                return "Malformed URL";
            case FORBIDDEN:
                return "Access Denied";
            case NO_REQUEST:
                return "No Request";
            case SERVER_ERROR:
                return "Server Error";
            case UNAUTHORIZED:
                return "Not Authorized";
            case CONNECTION_ERROR:
                return "Connection Failed";
        }
        return "Unknown Error";
    }
}
