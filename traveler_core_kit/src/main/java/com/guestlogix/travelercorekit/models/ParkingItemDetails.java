package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.BookingCategoryArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;
import com.guestlogix.travelercorekit.utilities.UrlArrayMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParkingItemDetails implements CatalogItemDetails {
    private String id;
    private String title;
    private String subTitle;
    private String description;
    private List<Attribute> information;
    private URL thumbnailURL;
    private List<URL> imageURLs;
    private List<Location> locations;
    private ContactInfo contact;
    private Supplier supplier;
    private String disclaimer;
    private String termsAndConditions;
    private Price price;
    private Price priceToPayOnline;
    private Price priceToPayOnsite;
    private ProductType productType;
    private List<BookingItemCategory> categories;
    private boolean isWishlisted;
    private ProviderTranslationAttribution providerTranslationAttribution;
    private Range<Date> dateRange;
    private Coordinate geolocation;

    private ParkingItemDetails(
            @NonNull String id,
            String title,
            String subTitle,
            String description,
            List<Attribute> information,
            @NonNull URL thumbnailURL,
            @NonNull List<URL> imageURLs,
            @NonNull List<Location> locations,
            ContactInfo contact,
            @NonNull Supplier supplier,
            String disclaimer,
            String termsAndConditions,
            @NonNull Price price,
            @NonNull Price priceToPayOnline,
            @NonNull Price priceToPayOnsite,
            @NonNull ProductType productType,
            @NonNull List<BookingItemCategory> categories,
            @NonNull boolean isWishlisted,
            ProviderTranslationAttribution providerTranslationAttribution,
            Range<Date> dateRange,
            Coordinate geolocation) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.information = information;
        this.thumbnailURL = thumbnailURL;
        this.imageURLs = imageURLs;
        this.locations = locations;
        this.contact = contact;
        this.supplier = supplier;
        this.disclaimer = disclaimer;
        this.termsAndConditions = termsAndConditions;
        this.price = price;
        this.priceToPayOnline = priceToPayOnline;
        this.priceToPayOnsite = priceToPayOnsite;
        this.productType = productType;
        this.categories = categories;
        this.isWishlisted = isWishlisted;
        this.providerTranslationAttribution = providerTranslationAttribution;
        this.dateRange = dateRange;
        this.geolocation = geolocation;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Attribute> getInformation() {
        return information;
    }

    public URL getThumbnailURL() {
        return thumbnailURL;
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

    public Price getPriceToPayOnline() {
        return priceToPayOnline;
    }

    public Price getPriceToPayOnsite() {
        return priceToPayOnsite;
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

    public Coordinate getGeolocation() {
        return geolocation;
    }

    public ProviderTranslationAttribution getProviderTranslationAttribution() {
        return providerTranslationAttribution;
    }

    public Range<Date> getDateRange() {
        return dateRange;
    }

    static class ParkingItemDetailsObjectMappingFactory implements ObjectMappingFactory<CatalogItemDetails> {
        @Override
        public ParkingItemDetails instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);
            String id = jsonObject.getString("id");
            String title = jsonObject.getNullableString("title");
            String subtitle = jsonObject.getNullableString("subTitle");
            String description = jsonObject.getNullableString("description");

            URL thumbnailURL = null;
            if (!jsonObject.isNull("thumbnail")) {
                thumbnailURL = new URL(jsonObject.getString("thumbnail"));
            }

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
            Price priceToPayOnline = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("priceToPayOnline").toString());
            Price priceToPayOnsite = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("priceToPayOnsite").toString());
            ProductType productType = ProductType.fromString(jsonObject.getString("purchaseStrategy"));

            //TODO: does parking even have category !!!!?
            List<BookingItemCategory> categories = new ArrayList<>();
            if (!jsonObject.isNull("categories"))
                categories = new BookingCategoryArrayMappingFactory().instantiate(jsonObject.getJSONArray("categories").toString());

            boolean isWishlisted = jsonObject.getBoolean("isWishlisted");
            ProviderTranslationAttribution providerTranslationAttribution = new ProviderTranslationAttribution.ProviderTranslationAttributionObjectMappingFactory().
                    instantiate(jsonObject.getJSONObject("providerTranslationAttribution").toString());
            String startTime = jsonObject.getString("startTime");
            String endTime = jsonObject.getString("endTime");
            Coordinate coordinate = new Coordinate.CoordinateObjectMappingFactory().instantiate(jsonObject.getJSONObject("geoLocation").toString());

            Assertion.eval(id != null);
            Assertion.eval(thumbnailURL != null);
            Assertion.eval(imageURLs != null);
            Assertion.eval(locations != null);
            Assertion.eval(supplier != null);
            Assertion.eval(price != null);
            Assertion.eval(priceToPayOnline != null);
            Assertion.eval(priceToPayOnsite != null);
            Assertion.eval(productType != null);
            Assertion.eval(categories != null);

            Range<Date> dateRange = new Range<>(DateHelper.parseISO8601(startTime), DateHelper.parseISO8601(endTime));

            return new ParkingItemDetails(
                    id,
                    title,
                    subtitle,
                    description,
                    information,
                    thumbnailURL,
                    imageURLs,
                    locations,
                    contact,
                    supplier,
                    disclaimer,
                    termsAndConditions,
                    price,
                    priceToPayOnline,
                    priceToPayOnsite,
                    productType,
                    categories,
                    isWishlisted,
                    providerTranslationAttribution,
                    dateRange,
                    coordinate);
        }
    }
}
