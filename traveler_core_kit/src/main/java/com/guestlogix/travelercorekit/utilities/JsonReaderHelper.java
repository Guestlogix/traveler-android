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
     * Performs a String read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return String
     * @throws IOException            If reading cannot be performed.
     * @throws ObjectMappingException If value is null
     */
    public static String readNonNullString(JsonReader reader) throws IOException, ObjectMappingException {

        String value = readString(reader);
        if (null == value || value.isEmpty()) {
            throw new ObjectMappingException("%s cannot be empty");
        }
        return value;
    }

    /**
     * Performs a Boolean read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return boolean
     * @throws IOException If reading cannot be performed.
     */
    public static boolean readBoolean(JsonReader reader) throws IOException, ObjectMappingException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            throw new ObjectMappingException("%s cannot be empty");
        } else {
            return reader.nextBoolean();
        }
    }

    /**
     * Performs a int read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return int or null
     * @throws IOException            If reading cannot be performed.
     * @throws ObjectMappingException If value is null
     */
    public static int readInteger(JsonReader reader) throws IOException, ObjectMappingException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            throw new ObjectMappingException("%s cannot be empty");
        } else {
            return reader.nextInt();
        }
    }

    /**
     * Performs a double read with null check from the reader object.
     *
     * @param reader Reader object to read from
     * @return double
     * @throws IOException            If reading cannot be performed.
     * @throws ObjectMappingException If value is null.
     */
    public static double readDouble(JsonReader reader) throws IOException, ObjectMappingException {
        JsonToken token = reader.peek();

        if (JsonToken.NULL == token) {
            reader.skipValue();
            throw new ObjectMappingException("%s cannot be empty");
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
     * Performs a int Array read from the reader object.
     *
     * @param reader Reader object to read from
     * @return int Array or empty array
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
