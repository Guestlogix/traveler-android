package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.util.JsonToken.BEGIN_ARRAY;

public class CatalogItemDetails implements Product {
    private String id;
    private Price price;
    private String title;
    private String description;
    private List<URL> imageURLs;
    private ContactInfo contact;
    private List<Location> locations;
    private PurchaseStrategy purchaseStrategy;
    private List<Attribute> information;

    private CatalogItemDetails(String id, String title, String description, List<URL> imageURLs, ContactInfo contact, List<Location> locations, Price priceStartingAt, PurchaseStrategy purchaseStrategy, List<Attribute> information) throws IllegalArgumentException {
        this.title = title;
        this.description = description;
        if (imageURLs == null) {
            throw new IllegalArgumentException("imageURLs can not be null");
        } else {
            this.imageURLs = imageURLs;
        }
        this.id = id;
        this.price = priceStartingAt;
        this.contact = contact;
        this.locations = locations;
        this.purchaseStrategy = purchaseStrategy;
        this.information = information;
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

    public String getDescription() {
        return description;
    }

    public List<URL> getImageURLs() {
        return imageURLs;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public PurchaseStrategy getPurchaseStrategy() {
        return purchaseStrategy;
    }

    public List<Attribute> getInformation() {
        return information;
    }

    /**
     * Factory class to construct CatalogItemDetails model from {@code JsonReader}.
     */
    static class CatalogItemDetailsObjectMappingFactory implements ObjectMappingFactory<CatalogItemDetails> {

        /**
         * Parses a reader object into CatalogItemDetails model.
         *
         * @param reader object to parse from.
         * @return CatalogItemDetails model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public CatalogItemDetails instantiate(JsonReader reader) throws Exception {
            String id = "";
            String title = "";
            String description = "";
            List<URL> imageURL = new ArrayList<>();
            ContactInfo contact = new ContactInfo();
            List<Location> locations = new ArrayList<>();
            Price priceStartingAt = new Price();
            PurchaseStrategy purchaseStrategy = PurchaseStrategy.Bookable;
            List<Attribute> information = new ArrayList<>();

            JsonToken token = reader.peek();
            if (BEGIN_ARRAY == token) {
                reader.beginArray();
            } else {
                reader.beginObject();
            }

            while (reader.hasNext()) {
                String key = reader.nextName();
                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "title":
                        title = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "imageUrls":
                        if (reader.peek() != JsonToken.NULL) {
                            imageURL.addAll(JsonReaderHelper.nextUrlArray(reader));
                        } else {
                            imageURL = null;
                            reader.skipValue();
                        }
                        break;
                    case "contact":
                        if (reader.peek() != JsonToken.NULL) {
                            contact = new ContactInfo.ContactInfoObjectMappingFactory().instantiate(reader);
                        } else {
                            contact = null;
                            reader.skipValue();
                        }
                        break;
                    case "locations":
                        if (reader.peek() != JsonToken.NULL) {
                            locations.addAll(new ArrayMappingFactory<>(new Location.LocationObjectMappingFactory()).instantiate(reader));
                        } else {
                            reader.skipValue();
                        }
                        break;
                    case "priceStartingAt":
                        if (reader.peek() != JsonToken.NULL) {
                            priceStartingAt = new Price.PriceObjectMappingFactory().instantiate(reader);
                        } else {
                            priceStartingAt = null;
                            reader.skipValue();
                        }
                        break;
                    case "purchaseStrategy":
                        purchaseStrategy = PurchaseStrategy.valueOf(reader.nextString());
                        break;
                    case "information":
                        if (reader.peek() != JsonToken.NULL) {
                            information.addAll(new ArrayMappingFactory<>(new Attribute.AttributeObjectMappingFactory()).instantiate(reader));
                        } else {
                            reader.skipValue();
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != imageURL, "imageUrls can not be null");

            return new CatalogItemDetails(id, title, description, imageURL, contact, locations, priceStartingAt, purchaseStrategy, information);
        }
    }
}
