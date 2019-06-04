package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;
import org.json.JSONException;

import java.lang.IllegalStateException;

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
     * @throws IOException           If reading cannot be performed.
     * @throws IllegalStateException If reader is unable to read String value
     */
    public static String readString(JsonReader reader) throws IllegalStateException, IOException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextString();
        }
    }

    /**
     * Performs a String read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return String
     * @throws IOException           If reading cannot be performed.
     * @throws JSONException         If value is null
     * @throws IllegalStateException If reader is unable to read String value
     */
    public static String readNonNullString(JsonReader reader) throws IOException, IllegalStateException, JSONException {

        String value = readString(reader);
        if (null == value) {
            throw new JSONException("null");
        } else if (value.isEmpty()) {
            throw new JSONException("empty");
        }
        return value;
    }

    /**
     * Performs a boolean read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return boolean
     * @throws IOException            If reading cannot be performed
     * @throws ObjectMappingException If value is null
     * @throws IllegalStateException  If reader is unable to read boolean value
     */
    public static boolean readBoolean(JsonReader reader) throws IOException, IllegalStateException, JSONException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            throw new JSONException("null");
        } else {
            return reader.nextBoolean();
        }
    }

    /**
     * Performs a int read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return int
     * @throws IOException           If reading cannot be performed
     * @throws JSONException         If value is null
     * @throws IllegalStateException If reader is unable to read int value
     */
    public static int readInteger(JsonReader reader) throws IOException, IllegalStateException, JSONException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            throw new JSONException("null");
        } else {
            return reader.nextInt();
        }
    }

    /**
     * Performs a double read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return double
     * @throws IOException           If reading cannot be performed.
     * @throws JSONException         If value is null.
     * @throws IllegalStateException If reader is unable to read double value
     */
    public static double readDouble(JsonReader reader) throws IOException, IllegalStateException, JSONException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            throw new JSONException("null");
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
        ArrayList<URL> urls = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            urls.add(new URL(reader.nextString()));
        }
        reader.endArray();
        return urls;
    }
}
