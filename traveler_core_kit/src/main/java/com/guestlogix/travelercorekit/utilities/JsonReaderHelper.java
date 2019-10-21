package com.guestlogix.travelercorekit.utilities;

import android.util.JsonReader;
import android.util.JsonToken;

import com.guestlogix.travelercorekit.models.CatalogItemCategory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonReaderHelper {
    public static String nextNullableString(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.skipValue();
            return null;
        } else {
            return reader.nextString();
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

    /**
     * Performs a URL Array read from the reader object.
     *
     * @param reader Reader object to read from
     * @return CatalogItemCategory Array or empty array
     * @throws IOException If reading cannot be performed.
     */
    public static List<CatalogItemCategory> readCatalogItemCategoryArray(JsonReader reader) throws IOException {
        ArrayList<CatalogItemCategory> categories = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            categories.add(CatalogItemCategory.fromString(reader.nextString()));
        }
        reader.endArray();
        return categories;
    }
}
