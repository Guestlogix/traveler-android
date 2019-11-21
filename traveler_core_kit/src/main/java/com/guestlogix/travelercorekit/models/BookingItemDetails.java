package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.util.JsonToken.NULL;

public class BookingItemDetails implements CatalogItemDetails {
    private String id;
    private String title;
    private String description;
    private List<Attribute> information;
    private List<URL> imageURLs;
    private List<Location> locations;
    private ContactInfo contact;
    private Supplier supplier;
    private String disclaimer;
    private String termsAndConditions;
    private Price price;
    private ProductType productType;
    private List<BookingItemCategory> categories;
    private boolean isWishlisted;
    private boolean isAvailable;
    private ProviderTranslationAttribution providerTranslationAttribution;

    private BookingItemDetails(
            @NonNull String id,
            String title,
            String description,
            List<Attribute> information,
            @NonNull List<URL> imageURLs,
            @NonNull List<Location> locations,
            ContactInfo contact,
            @NonNull Supplier supplier,
            String disclaimer,
            String termsAndConditions,
            @NonNull Price price,
            @NonNull ProductType productType,
            @NonNull List<BookingItemCategory> categories,
            @NonNull boolean isWishlisted,
            @NonNull boolean isAvailable,
            ProviderTranslationAttribution providerTranslationAttribution) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.information = information;
        this.imageURLs = imageURLs;
        this.locations = locations;
        this.contact = contact;
        this.supplier = supplier;
        this.disclaimer = disclaimer;
        this.termsAndConditions = termsAndConditions;
        this.price = price;
        this.productType = productType;
        this.categories = categories;
        this.isWishlisted = isWishlisted;
        this.isAvailable = isAvailable;
        this.providerTranslationAttribution = providerTranslationAttribution;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Attribute> getInformation() {
        return information;
    }

    @Override
    public List<URL> getImageURLs() {
        return imageURLs;
    }

    @Override
    public List<Location> getLocations() {
        return locations;
    }

    @Override
    public ContactInfo getContact() {
        return contact;
    }

    @Override
    public Supplier getSupplier() {
        return supplier;
    }

    @Override
    public String getDisclaimer() {
        return disclaimer;
    }

    @Override
    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public Price getPrice() {
        return price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public List<BookingItemCategory> getCategories() {
        return categories;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public ProviderTranslationAttribution getProviderTranslationAttribution() {
        return providerTranslationAttribution;
    }

    static class BookingItemDetailsObjectMappingFactory implements ObjectMappingFactory<CatalogItemDetails> {
        @Override
        public BookingItemDetails instantiate(JsonReader reader) throws Exception {
            String id = null;
            String title = null;
            String description = null;
            List<Attribute> information = null;
            List<URL> imageURLs = null;
            List<Location> locations = null;
            ContactInfo contact = null;
            Supplier supplier = null;
            String disclaimer = null;
            String termsAndConditions = null;
            Price price = null;
            ProductType productType = null;
            List<BookingItemCategory> categories = null;
            boolean isWishlisted = false;
            boolean isAvailable = false;
            ProviderTranslationAttribution providerTranslationAttribution = null;

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
                    case "information":
                        if (reader.peek() == NULL) {
                            information = new ArrayList<>();
                            reader.skipValue();
                        } else {
                            information = new ArrayMappingFactory<>(new Attribute.AttributeObjectMappingFactory())
                                    .instantiate(reader);
                        }
                        break;
                    case "imageUrls":
                        if (reader.peek() == NULL) {
                            imageURLs = new ArrayList<>();
                            reader.skipValue();
                        } else {
                            imageURLs = JsonReaderHelper.readURLArray(reader);
                        }
                        break;
                    case "locations":
                        if (reader.peek() == NULL)
                            reader.skipValue();
                        else
                            locations = new ArrayMappingFactory<>(new Location.LocationObjectMappingFactory())
                                    .instantiate(reader);
                        break;
                    case "contact":
                        if (reader.peek() == NULL)
                            reader.skipValue();
                        else
                            contact = new ContactInfo.ContactInfoObjectMappingFactory().instantiate(reader);
                        break;
                    case "supplier":
                        supplier = new Supplier.SupplierObjectMappingFactory().instantiate(reader);
                        break;
                    case "disclaimer":
                        disclaimer = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "termsAndConditions":
                        termsAndConditions = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "priceStartingAt":
                        price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "purchaseStrategy":
                        productType = ProductType.fromString(reader.nextString());
                        break;
                    case "categories":
                        if (reader.peek() == NULL) {
                            categories = new ArrayList<>();
                            reader.skipValue();
                        } else {
                            categories = JsonReaderHelper.readCatalogItemCategoryArray(reader);
                        }
                        break;
                    case "isWishlisted":
                        if (reader.peek() == NULL) {
                            reader.skipValue();
                        } else {
                            isWishlisted = reader.nextBoolean();
                        }
                        break;
                    case "isAvailable":
                        isAvailable = reader.nextBoolean();
                        break;
                    case "providerTranslationAttribution":
                        providerTranslationAttribution = new ProviderTranslationAttribution.ProviderTranslationAttributionObjectMappingFactory().
                                instantiate(reader);
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
            Assertion.eval(supplier != null);
            Assertion.eval(price != null);
            Assertion.eval(productType != null);
            Assertion.eval(categories != null);

            return new BookingItemDetails(
                    id,
                    title,
                    description,
                    information,
                    imageURLs,
                    locations,
                    contact,
                    supplier,
                    disclaimer,
                    termsAndConditions,
                    price,
                    productType,
                    categories,
                    isWishlisted,
                    isAvailable,
                    providerTranslationAttribution);
        }
    }
}