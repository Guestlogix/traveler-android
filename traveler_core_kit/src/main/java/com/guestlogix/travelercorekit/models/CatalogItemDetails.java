package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.ObjectMappingException;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
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
         * @param reader Object to parse from.
         * @return CatalogItemDetails model object from the reader.
         * @throws ObjectMappingException if mapping fails or missing any required field.
         */
        @Override
        public CatalogItemDetails instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "CatalogItemDetails";
            try {
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
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                else if (BEGIN_ARRAY == token) {
                    reader.beginArray();
                } else {
                    reader.beginObject();
                }

                while (reader.hasNext()) {
                    key = reader.nextName();
                    switch (key) {
                        case "id":
                            id = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "title":
                            title = JsonReaderHelper.readString(reader);
                            break;
                        case "description":
                            description = JsonReaderHelper.readString(reader);
                            break;
                        case "imageUrls":
                            if (reader.peek() != JsonToken.NULL) {
                                imageURL.addAll(JsonReaderHelper.readURLArray(reader));
                            } else {
                                reader.skipValue();
                            }
                            break;
                        case "contact":
                            contact = new ContactInfo.ContactInfoObjectMappingFactory().instantiate(reader);
                            break;
                        case "locations":
                            locations.addAll(new ArrayMappingFactory<>(new Location.LocationObjectMappingFactory()).instantiate(reader));
                            break;
                        case "priceStartingAt":
                            priceStartingAt = new Price.PriceObjectMappingFactory().instantiate(reader);
                            break;
                        case "purchaseStrategy":
                            purchaseStrategy = PurchaseStrategy.valueOf(JsonReaderHelper.readString(reader));
                            break;
                        case "information":
                            information.addAll(new ArrayMappingFactory<>(new Attribute.AttributeObjectMappingFactory()).instantiate(reader));
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new CatalogItemDetails(id, title, description, imageURL, contact, locations, priceStartingAt, purchaseStrategy, information);

            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
