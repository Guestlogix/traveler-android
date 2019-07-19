package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CatalogItem implements Product {
    private String id;
    private String title;
    private String subTitle;
    private URL imageURL;
    private Price price;

    private CatalogItem(@NonNull String id, String title, String subTitle, URL imageURL, Price price) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
        this.price = price;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Price getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public URL getImageURL() {
        return imageURL;
    }

    static class CatalogItemObjectMappingFactory implements ObjectMappingFactory<CatalogItem> {
        @Override
        public CatalogItem instantiate(JsonReader reader) throws Exception {
            String id = null;
            String title = null;
            String subTitle = null;
            URL thumbnail = null;
            Price price = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "subTitle":
                        subTitle = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "thumbnail":
                        JsonToken token = reader.peek();

                        if (token == JsonToken.NULL) {
                            reader.skipValue();
                            break;
                        }

                        thumbnail = new URL(reader.nextString());
                        break;
                    case "priceStartingAt":
                        price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(price != null);

            return new CatalogItem(id, title, subTitle, thumbnail, price);
        }
    }
}