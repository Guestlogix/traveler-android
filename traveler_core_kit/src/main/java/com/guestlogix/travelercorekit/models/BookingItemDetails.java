package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.BookingCategoryArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.UrlArrayMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public List<URL> getImageUrls() {
        return imageURLs;
    }

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
        public BookingItemDetails instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String id = jsonObject.getString("id");
            String title = jsonObject.getNullableString("title");

            String description = jsonObject.getNullableString("description");

            List<Attribute> information = new ArrayList<>();
            if (!jsonObject.isNull("information"))
                information = new ArrayMappingFactory<>(new Attribute.AttributeObjectMappingFactory())
                        .instantiate(jsonObject.getJSONArray("information").toString());

            List<URL> imageURLs = new ArrayList<>();
            if (!jsonObject.isNull("imageUrls"))
                imageURLs = new UrlArrayMappingFactory().instantiate(jsonObject.getJSONArray("imageUrls").toString());

            List<Location> locations = null;
            if (!jsonObject.isNull("locations"))
                locations = new ArrayMappingFactory<>(new Location.LocationObjectMappingFactory()).instantiate(jsonObject.getJSONArray("locations").toString());


            ContactInfo contact = null;
            if (!jsonObject.isNull("contact"))
                contact = new ContactInfo.ContactInfoObjectMappingFactory().instantiate(jsonObject.getJSONObject("contact").toString());

            Supplier supplier = new Supplier.SupplierObjectMappingFactory().instantiate(jsonObject.getJSONObject("supplier").toString());
            String disclaimer = jsonObject.getNullableString("disclaimer");
            String termsAndConditions = jsonObject.getNullableString("termsAndConditions");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("priceStartingAt").toString());
            ProductType productType = ProductType.fromString(jsonObject.getString("purchaseStrategy"));

            List<BookingItemCategory> categories = new ArrayList<>();
            if (!jsonObject.isNull("categories"))
                categories = new BookingCategoryArrayMappingFactory().instantiate(jsonObject.getJSONArray("categories").toString());

            boolean isWishlisted = false;
            if (!jsonObject.isNull("isWishlisted"))
                isWishlisted = jsonObject.getBoolean("isWishlisted");

            boolean isAvailable = jsonObject.getBoolean("isAvailable");

            ProviderTranslationAttribution providerTranslationAttribution = new ProviderTranslationAttribution.ProviderTranslationAttributionObjectMappingFactory().
                    instantiate(jsonObject.getJSONObject("providerTranslationAttribution").toString());

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