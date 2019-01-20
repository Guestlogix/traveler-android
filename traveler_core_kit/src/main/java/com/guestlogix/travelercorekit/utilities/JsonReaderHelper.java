package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Performs a String Array read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return String Array or null
     * @throws IOException If reading cannot be performed.
     */
    public static ArrayList<String> readStringsArray(JsonReader reader) throws IOException {
        ArrayList<String> strings = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            strings.add(reader.nextString());
        }
        reader.endArray();
        return strings;
    }

    /**
     * Performs a String Array read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return Integer Array or null
     * @throws IOException If reading cannot be performed.
     */
    public static ArrayList<Integer> readIntegerArray(JsonReader reader) throws IOException {
        ArrayList<Integer> ints = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ints.add(reader.nextInt());
        }
        reader.endArray();
        return ints;
    }

    /**
     * Performs a String Array read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return Long Array or null
     * @throws IOException If reading cannot be performed.
     */
    public static ArrayList<Long> readLongArray(JsonReader reader) throws IOException {
        ArrayList<Long> ints = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ints.add(reader.nextLong());
        }
        reader.endArray();
        return ints;
    }

}
