package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.net.URL;

public class Trademark implements Serializable {
    private URL imageURL;
    private String copyright;

    Trademark(@NonNull URL imageURL, @NonNull String copyright) {
        this.imageURL = imageURL;
        this.copyright = copyright;
    }

    public URL getIconURL() {
        return imageURL;
    }

    public String getCopyright() {
        return copyright;
    }

    public static class TrademarkObjectMappingFactory implements ObjectMappingFactory<Trademark> {
        @Override
        public Trademark instantiate(JsonReader reader) throws Exception {
            URL imageURL = null;
            String copyright = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                if (key.equals("iconUrl")) {
                    String url = reader.nextString();
                    imageURL = new URL(url);
                } else if (key.equals("copyRight")) {
                    copyright = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(copyright != null);
            Assertion.eval(imageURL != null);

            return new Trademark(imageURL, copyright);
        }
    }
}
