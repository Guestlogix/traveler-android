package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    private boolean isWishlisted;
    private Supplier supplier;
    private String termsAndConditions;
    private String disclaimer;

    private CatalogItemDetails(@NonNull String id,
                               String title,
                               String description,
                               @NonNull List<URL> imageURLs,
                               ContactInfo contact,
                               @NonNull List<Location> locations,
                               @NonNull Price priceStartingAt,
                               @NonNull PurchaseStrategy purchaseStrategy,
                               List<Attribute> information,
                               boolean isWishlisted,
                               @NonNull Supplier supplier,
                               String termsAndConditions,
                               String disclaimer) {
        this.title = title;
        this.description = description;
        this.imageURLs = imageURLs;
        this.id = id;
        this.price = priceStartingAt;
        this.contact = contact;
        this.locations = locations;
        this.purchaseStrategy = purchaseStrategy;
        this.information = information;
        this.isWishlisted = isWishlisted;
        this.supplier = supplier;
        this.termsAndConditions = termsAndConditions;
        this.disclaimer = disclaimer;
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

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public String getDisclaimer() {
        return disclaimer;
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
            boolean isWishlisted = false;
            Supplier supplier = null;
            String termsAndConditions = null;
            String disclaimer = null;

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
                    case "isWishlisted":
                        if (reader.peek() == NULL) {
                            isWishlisted = false;
                            reader.skipValue();
                        } else {
                            isWishlisted = reader.nextBoolean();
                        }
                        break;
                    case "supplier":
                        supplier = new Supplier.SupplierObjectMappingFactory().instantiate(reader);
                        break;
                    case "termsAndConditions":
                        termsAndConditions = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "disclaimer":
                        disclaimer = JsonReaderHelper.nextNullableString(reader);
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

            return new CatalogItemDetails(id,
                    title,
                    description,
                    imageURLs,
                    contact,
                    locations,
                    priceStartingAt,
                    purchaseStrategy,
                    information,
                    isWishlisted,
                    supplier,
                    termsAndConditions,
                    disclaimer);
        }
    }
}
