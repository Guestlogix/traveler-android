package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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
        public ProviderTranslationAttribution instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            URL image = null;
            if (!jsonObject.isNull( "image"))
                image = new URL(jsonObject.getString( "image"));

            URL link = null;
            if (!jsonObject.isNull( "link"))
                link = new URL(jsonObject.getString( "link"));

            return new ProviderTranslationAttribution(image, link);
        }
    }
}
