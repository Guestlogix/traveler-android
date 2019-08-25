package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.util.JsonToken.BEGIN_ARRAY;
import static android.util.JsonToken.NULL;

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
    private Supplier supplier;

    private CatalogItemDetails(@NonNull String id, String title, String description, @NonNull List<URL> imageURLs, ContactInfo contact, @NonNull List<Location> locations, @NonNull Price priceStartingAt, @NonNull PurchaseStrategy purchaseStrategy, List<Attribute> information, @NonNull Supplier supplier) {
        this.title = title;
        this.description = description;
        this.imageURLs = imageURLs;
        this.id = id;
        this.price = priceStartingAt;
        this.contact = contact;
        this.locations = locations;
        this.purchaseStrategy = purchaseStrategy;
        this.information = information;
        this.supplier = supplier;
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

    static class CatalogItemDetailsObjectMappingFactory implements ObjectMappingFactory<CatalogItemDetails> {
        @Override
        public CatalogItemDetails instantiate(JsonReader reader) throws Exception {
            String id = null;
            String title = null;
            String description = null;
            List<URL> imageURLs = null;
            ContactInfo contact = null;
            List<Location> locations = null;
            Price priceStartingAt = null;
            PurchaseStrategy purchaseStrategy = null;
            List<Attribute> information = null;
            Supplier supplier = null;

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
                    case "description":
                        description = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "imageUrls":
                        if (reader.peek() != JsonToken.NULL) {
                            imageURLs = JsonReaderHelper.readURLArray(reader);
                        } else {
                            imageURLs = new ArrayList<>();
                            reader.skipValue();
                        }
                        break;
                    case "contact":
                        if (reader.peek() == NULL)
                            reader.skipValue();
                        else
                            contact = new ContactInfo.ContactInfoObjectMappingFactory().instantiate(reader);
                        break;
                    case "locations":
                        if (reader.peek() == NULL)
                            reader.skipValue();
                        else
                            locations = new ArrayMappingFactory<>(new Location.LocationObjectMappingFactory()).instantiate(reader);
                        break;
                    case "priceStartingAt":
                        priceStartingAt = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "purchaseStrategy":
                        purchaseStrategy = PurchaseStrategy.valueOf(reader.nextString());
                        break;
                    case "information":
                        if (reader.peek() == NULL) {
                            information = new ArrayList<>();
                            reader.skipValue();
                        } else {
                            information = new ArrayMappingFactory<>(new Attribute.AttributeObjectMappingFactory()).instantiate(reader);
                        }
                        break;
                    case "supplier":
                        supplier = new Supplier.SupplierObjectMappingFactory().instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(imageURLs != null);
            Assertion.eval(locations != null);
            Assertion.eval(priceStartingAt != null);
            Assertion.eval(purchaseStrategy != null);
            Assertion.eval(supplier != null);

            return new CatalogItemDetails(id, title, description, imageURLs, contact, locations, priceStartingAt, purchaseStrategy, information, supplier);
        }
    }
}
