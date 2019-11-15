package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

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
        public Trademark instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String iconUrl = jsonObject.getString("iconUrl");
            URL imageURL = null;
            if (iconUrl != null)
                imageURL = new URL(iconUrl);
            String copyright = jsonObject.getString("copyRight");

            Assertion.eval(copyright != null);
            Assertion.eval(imageURL != null);

            return new Trademark(imageURL, copyright);
        }
    }
}
