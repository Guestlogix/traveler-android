package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.util.JsonToken.BEGIN_ARRAY;

public class CatalogItemDetails extends Product {

    private String title;
    private String description;
    private List<String> imageURL;
    private ContactInfo contact;
    private List<Location> locations;
    private Price priceStartingAt;
    private String purchaseStrategy;
    private List<Attribute> information;

    public CatalogItemDetails(String id, String title, String description, List<String> imageURL, ContactInfo contact, List<Location> locations, Price priceStartingAt, String purchaseStrategy, List<Attribute> information) {
        super(id,priceStartingAt);
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.contact = contact;
        this.locations = locations;
        this.priceStartingAt = priceStartingAt;
        this.purchaseStrategy = purchaseStrategy;
        this.information = information;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageURL() {
        return imageURL;
    }

    public void setImageURL(List<String> imageURL) {
        this.imageURL = imageURL;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public void setContact(ContactInfo contact) {
        this.contact = contact;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Price getPriceStartingAt() {
        return priceStartingAt;
    }

    public void setPriceStartingAt(Price priceStartingAt) {
        this.priceStartingAt = priceStartingAt;
    }

    public String getPurchaseStrategy() {
        return purchaseStrategy;
    }

    public void setPurchaseStrategy(String purchaseStrategy) {
        this.purchaseStrategy = purchaseStrategy;
    }

    public List<Attribute> getInformation() {
        return information;
    }

    public void setInformation(List<Attribute> information) {
        this.information = information;
    }


    public static class CatalogItemDetailsObjectMappingFactory implements ObjectMappingFactory<CatalogItemDetails> {

        @Override
        public CatalogItemDetails instantiate(JsonReader reader) throws IOException, ObjectMappingException {
            return readItem(reader);
        }

        private CatalogItemDetails readItem(JsonReader reader) throws IOException, ObjectMappingException {
            String id = "";
            String title = "";
            String description = "";
            List<String> imageURL = new ArrayList<>();
            ContactInfo contact = new ContactInfo();
            List<Location> locations = new ArrayList<>();
            Price priceStartingAt = new Price();
            String purchaseStrategy = "";
            List<Attribute> information = new ArrayList<>();

            if (BEGIN_ARRAY == reader.peek()) {
                reader.beginArray();
            } else {
                reader.beginObject();
            }

            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "id":
                        id = JsonReaderHelper.readString(reader);
                        break;
                    case "title":
                        title = JsonReaderHelper.readString(reader);
                        break;
                    case "description":
                        description = JsonReaderHelper.readString(reader);
                        break;
                    case "imageUrls":
                        if (reader.peek() != JsonToken.NULL) {
                            imageURL.addAll(JsonReaderHelper.readStringsArray(reader));
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
                        purchaseStrategy = JsonReaderHelper.readString(reader);
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
        }
    }
}
