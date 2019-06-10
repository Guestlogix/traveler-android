package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import java.lang.IllegalStateException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonReaderHelper {
    /**
     * Performs a String read with null check from the reader object.
     *
     * @param reader {@link JsonReader} object to read from.
     * @return {@link java.lang.String} or null.
     * @throws IllegalStateException if reader is unable to read String value.
     * @throws IOException           if reading cannot be performed.
     */
    public static String nextNullableString(JsonReader reader) throws IllegalStateException, IOException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextString();
        }
    }

    /**
     * Performs a boolean read with null check from the reader object.
     *
     * @param reader {@link JsonReader} object to read from.
     * @return {@link java.lang.Boolean} value or null.
     * @throws IOException           if reading cannot be performed.
     * @throws IllegalStateException if reader is unable to read boolean value.
     */
    public static Boolean nextNullableBoolean(JsonReader reader) throws IOException, IllegalStateException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextBoolean();
        }
    }

    /**
     * Performs a int read with null check from the reader object.
     *
     * @param reader {@link JsonReader} object to read from.
     * @return {@link java.lang.Integer} value or null.
     * @throws IOException           if reading cannot be performed.
     * @throws IllegalStateException if reader is unable to read int value.
     */
    public static Integer nextNullableInteger(JsonReader reader) throws IOException, IllegalStateException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextInt();
        }
    }

    /**
     * Performs a double read with null check from the reader object.
     *
     * @param reader {@link JsonReader} object to read from.
     * @return {@link java.lang.Double} value or null.
     * @throws IOException           if reading cannot be performed.
     * @throws IllegalStateException if reader is unable to read double value.
     */
    public static Double nextNullableDouble(JsonReader reader) throws IOException, IllegalStateException {
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
     * @param reader {@link JsonReader} object to read from.
     * @return {@link java.lang.String} Array or empty Array.
     * @throws IOException if reading cannot be performed.
     */
    public static List<String> nextStringsArray(JsonReader reader) throws IOException {
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
     * @param reader {@link JsonReader} object to read from.
     * @return {@link java.lang.Integer} Array or empty Array.
     * @throws IOException if reading cannot be performed.
     */
    public static List<Integer> nextIntegerArray(JsonReader reader) throws IOException {
        ArrayList<Integer> ints = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ints.add(JsonReaderHelper.nextNullableInteger(reader));
        }

        reader.endArray();
        return ints;
    }

    /**
     * Performs a Long Array read from the reader object.
     *
     * @param reader {@link JsonReader} object to read from.
     * @return {@link java.lang.Long} Array or empty Array.
     * @throws IOException if reading cannot be performed.
     */
    public static List<Long> nextLongArray(JsonReader reader) throws IOException {
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
     * @param reader {@link JsonReader} object to read from.
     * @return {@link URL} Array or empty Array.
     * @throws IOException if reading cannot be performed.
     */
    public static List<URL> nextUrlArray(JsonReader reader) throws IOException {
        ArrayList<URL> urls = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            urls.add(new URL(reader.nextString()));
        }

        reader.endArray();
        return urls;
    }
}