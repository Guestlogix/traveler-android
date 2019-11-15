package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.UrlArrayMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PartnerOfferingItemDetails implements CatalogItemDetails {

    private String title;
    private String description;
    private List<URL> imageURLs;
    private List<Attribute> information;
    private Price price;
    private ContactInfo contact;
    private Supplier supplier;
    private String disclaimer;
    private String termsAndConditions;
    private List<PartnerOfferingGroup> offeringGroups;
    private ProductType productType;

    private PartnerOfferingItemDetails(
            String title,
            String description,
            List<Attribute> information,
            @NonNull List<URL> imageURLs,
            ContactInfo contact,
            @NonNull Supplier supplier,
            String disclaimer,
            String termsAndConditions,
            @NonNull Price price,
            List<PartnerOfferingGroup> offeringGroups,
            ProductType productType) {
        this.title = title;
        this.description = description;
        this.information = information;
        this.imageURLs = imageURLs;
        this.contact = contact;
        this.supplier = supplier;
        this.disclaimer = disclaimer;
        this.termsAndConditions = termsAndConditions;
        this.price = price;
        this.offeringGroups = offeringGroups;
        this.productType = productType;
    }

    @Override
    public ProductType getProductType() {
        return productType;
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

    public List<URL> getImageURLs() {
        return imageURLs;
    }

    public List<PartnerOfferingGroup> getOfferingGroups() {
        return offeringGroups;
    }

    static class PartnerOfferingItemDetailsObjectMappingFactory implements ObjectMappingFactory<CatalogItemDetails> {
        @Override
        public PartnerOfferingItemDetails instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String title = jsonObject.getNullableString("title");

            String description = jsonObject.getNullableString("description");
            List<PartnerOfferingGroup> offeringGroups = new ArrayMappingFactory<PartnerOfferingGroup>(new PartnerOfferingGroup.PartnerOfferingGroupObjectMappingFactory()).instantiate(jsonObject.getJSONArray("menu").toString());

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


            Assertion.eval(imageURLs != null);
            Assertion.eval(locations != null);
            Assertion.eval(supplier != null);
            Assertion.eval(price != null);

            return new PartnerOfferingItemDetails(
                    title,
                    description,
                    information,
                    imageURLs,
                    contact,
                    supplier,
                    disclaimer,
                    termsAndConditions,
                    price,
                    offeringGroups,
                    ProductType.PARTNER_OFFERING);
        }
    }
}