package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.net.URL;
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
     * Performs a String Array read from the reader object.
     *
     * @param reader Reader object to read from
     * @return String Array or empty array
     * @throws IOException If reading cannot be performed.
     */
    public static List<String> readStringsArray(JsonReader reader) throws IOException {
        ArrayList<String> strings = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            strings.add(reader.nextString());
        }
        reader.endArray();
        return strings;
    }

    /**
     * Performs a Integer Array read from the reader object.
     *
     * @param reader Reader object to read from
     * @return Integer Array or empty array
     * @throws IOException If reading cannot be performed.
     */
    public static List<Integer> readIntegerArray(JsonReader reader) throws IOException {
        ArrayList<Integer> ints = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ints.add(reader.nextInt());
        }
        reader.endArray();
        return ints;
    }

    /**
     * Performs a Long Array read from the reader object.
     *
     * @param reader Reader object to read from
     * @return Long Array or empty array
     * @throws IOException If reading cannot be performed.
     */
    public static List<Long> readLongArray(JsonReader reader) throws IOException {
        ArrayList<Long> longs = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            longs.add(reader.nextLong());
        }
        reader.endArray();
        return longs;
    }

    /**
     * Performs a URL Array read from the reader object.
     *
     * @param reader Reader object to read from
     * @return URL Array or empty array
     * @throws IOException If reading cannot be performed.
     */
    public static List<URL> readURLArray(JsonReader reader) throws IOException {
        ArrayList<URL> strings = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            strings.add(new URL(reader.nextString()));
        }
        reader.endArray();
        return strings;
    }
}
