package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;

public class JsonReaderHelper {
    /**
     * Performs a String read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return String or null
     * @throws IOException If reading cannot be performed.
     */
    public static String readString(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextString();
        }
    }

    /**
     * Performs a Boolean read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return Boolean or null
     * @throws IOException If reading cannot be performed.
     */
    public static Boolean readBoolean(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextBoolean();
        }
    }

    /**
     * Performs a Integer read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return Integer or null
     * @throws IOException If reading cannot be performed.
     */
    public static Integer readInteger(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextInt();
        }
    }

    /**
     * Performs a Long read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return Long or null
     * @throws IOException If reading cannot be performed.
     */
    public static Long readLong(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextLong();
        }
    }

    /**
     * Performs a Double read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return Double or null
     * @throws IOException If reading cannot be performed.
     */
    public static Double readDouble(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextDouble();
        }
    }
}
