package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.util.JsonToken.BEGIN_ARRAY;

public class CatalogItemDetails extends Product {

    private String title;
    private String description;
    private List<URL> imageURLs;
    private ContactInfo contact;
    private List<Location> locations;
    private Price priceStartingAt;
    private PurchaseStrategy purchaseStrategy;
    private List<Attribute> information;


    public CatalogItemDetails(String id, String title, String description, List<URL> imageURLs, ContactInfo contact, List<Location> locations, Price priceStartingAt, PurchaseStrategy purchaseStrategy, List<Attribute> information) {
        super(id,priceStartingAt);
        this.title = title;
        this.description = description;
        this.imageURLs = imageURLs;
        this.contact = contact;
        this.locations = locations;
        this.priceStartingAt = priceStartingAt;
        this.purchaseStrategy = purchaseStrategy;
        this.information = information;
    }

    public String getId() {
        return id;
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

    public Price getPriceStartingAt() {
        return priceStartingAt;
    }

    public PurchaseStrategy getPurchaseStrategy() {
        return purchaseStrategy;
    }

    public List<Attribute> getInformation() {
        return information;
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
            List<URL> imageURL = new ArrayList<>();
            ContactInfo contact = new ContactInfo();
            List<Location> locations = new ArrayList<>();
            Price priceStartingAt = new Price();
            PurchaseStrategy purchaseStrategy = PurchaseStrategy.Bookable;
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
                            imageURL.addAll(JsonReaderHelper.readURLArray(reader));
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
        }
    }
}
