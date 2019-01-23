package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import com.guestlogix.travelercorekit.network.ArrayMappingFactory;
import com.guestlogix.travelercorekit.network.ObjectMappingException;
import com.guestlogix.travelercorekit.network.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import static android.util.JsonToken.BEGIN_ARRAY;

public class CatalogItemDetails {

    private String id;
    private String title;
    private String description;
    private ArrayList<String> imageURL;
    private ContactInfo contact;
    private ArrayList<Location> locations;
    private double priceStartingAt;
    private String purchaseStrategy;
    private ArrayList<Attribute> information;

    public CatalogItemDetails(String id, String title, String description, ArrayList<String> imageURL, ContactInfo contact, ArrayList<Location> locations, double priceStartingAt, String purchaseStrategy, ArrayList<Attribute> information) {
        this.id = id;
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

    public ArrayList<String> getImageURL() {
        return imageURL;
    }

    public void setImageURL(ArrayList<String> imageURL) {
        this.imageURL = imageURL;
    }

    public ContactInfo getContact() {
        return contact;
    }

    public void setContact(ContactInfo contact) {
        this.contact = contact;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public double getPriceStartingAt() {
        return priceStartingAt;
    }

    public void setPriceStartingAt(double priceStartingAt) {
        this.priceStartingAt = priceStartingAt;
    }

    public String getPurchaseStrategy() {
        return purchaseStrategy;
    }

    public void setPurchaseStrategy(String purchaseStrategy) {
        this.purchaseStrategy = purchaseStrategy;
    }

    public ArrayList<Attribute> getInformation() {
        return information;
    }

    public void setInformation(ArrayList<Attribute> information) {
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
            ArrayList<String> imageURL = new ArrayList<>();
            ContactInfo contact = new ContactInfo();
            ArrayList<Location> locations = new ArrayList<>();
            double priceStartingAt = 0.0;
            String purchaseStrategy = "";
            ArrayList<Attribute> information = new ArrayList<>();

            if (BEGIN_ARRAY == reader.peek()) {
                reader.beginArray();
            } else {
                reader.beginObject();
            }

            while (reader.hasNext()) {
                String name = reader.nextName();
                Log.d("CatalogItemDetail", "Key:" + name);
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
                            imageURL = JsonReaderHelper.readStringsArray(reader);
                        }
                        break;
                    case "contact":
                        contact = new ContactInfo.ContactInfoObjectMappingFactory().instantiate(reader);
                        break;
                    case "locations":
                        locations = new ArrayMappingFactory<>(new Location.LocationObjectMappingFactory()).instantiate(reader);
                        break;
                    case "priceStartingAt":
                        priceStartingAt = JsonReaderHelper.readDouble(reader);
                        break;
                    case "purchaseStrategy":
                        purchaseStrategy = JsonReaderHelper.readString(reader);
                        break;
                    case "information":
                        information = new ArrayMappingFactory<>(new Attribute.AttributeObjectMappingFactory()).instantiate(reader);
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
