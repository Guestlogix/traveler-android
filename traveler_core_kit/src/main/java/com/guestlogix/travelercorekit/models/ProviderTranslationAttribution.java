package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.net.URL;

public class ProviderTranslationAttribution implements Serializable {
    private URL imageLink;
    private URL translationLink;

    private ProviderTranslationAttribution(URL imageLink, URL translationLink) {
        this.imageLink = imageLink;
        this.translationLink = translationLink;
    }

    public URL getImageLink() {
        return imageLink;
    }

    public URL getTranslationLink() {
        return translationLink;
    }

    static class ProviderTranslationAttributionObjectMappingFactory implements ObjectMappingFactory<ProviderTranslationAttribution> {

        @Override
        public ProviderTranslationAttribution instantiate(JsonReader reader) throws Exception {
            URL image = null;
            URL link = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();
                JsonToken token;

                switch (key) {
                    case "imageLink":
                        token = reader.peek();

                        if (token == JsonToken.NULL) {
                            reader.skipValue();
                            break;
                        }

                        image = new URL(reader.nextString());
                        break;
                    case "translationLink":
                        token = reader.peek();

                        if (token == JsonToken.NULL) {
                            reader.skipValue();
                            break;
                        }

                        link = new URL(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            return new ProviderTranslationAttribution(image, link);
        }
    }
}
