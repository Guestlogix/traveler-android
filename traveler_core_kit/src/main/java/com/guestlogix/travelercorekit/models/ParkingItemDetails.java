package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;

import androidx.annotation.NonNull;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JsonReaderHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.util.JsonToken.NULL;

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
        public ParkingItemDetails instantiate(JsonReader reader) throws Exception {
            String id = null;
            String title = null;
            String subtitle = null;
            String description = null;
            List<Attribute> information = null;
            URL thumbnailURL = null;
            List<URL> imageURLs = null;
            List<Location> locations = null;
            ContactInfo contact = null;
            Supplier supplier = null;
            String disclaimer = null;
            String termsAndConditions = null;
            Price price = null;
            Price payableOnline = null;
            Price payableOnsite = null;
            ProductType productType = null;
            List<BookingItemCategory> categories = null;
            boolean isWishlisted = false;
            ProviderTranslationAttribution providerTranslationAttribution = null;
            String startTime = null;
            String endTime = null;
            Coordinate coordinate = null;

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
                    case "subTitle":
                        subtitle = JsonReaderHelper.nextNullableString(reader);
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
                    case "thumbnail":
                        JsonToken token = reader.peek();

                        if (token == JsonToken.NULL) {
                            reader.skipValue();
                            break;
                        }

                        thumbnailURL = new URL(reader.nextString());
                        break;
                    case "imageUrls":
                        if (reader.peek() != JsonToken.NULL) {
                            imageURLs = JsonReaderHelper.readURLArray(reader);
                        } else {
                            imageURLs = new ArrayList<>();
                            reader.skipValue();
                        }
                        break;
                    case "locations":
                        if (reader.peek() == NULL)
                            reader.skipValue();
                        else
                            locations = new ArrayMappingFactory<>(new Location.LocationObjectMappingFactory()).instantiate(reader);
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
                    case "payableOnline":
                        payableOnline = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "payableOnsite":
                        payableOnsite = new Price.PriceObjectMappingFactory().instantiate(reader);
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
                    case "providerTranslationAttribution":
                        if (reader.peek() == NULL) {
                            reader.skipValue();
                        } else {
                            providerTranslationAttribution = new ProviderTranslationAttribution.ProviderTranslationAttributionObjectMappingFactory().
                                    instantiate(reader);
                        }
                        break;
                    case "startTime":
                        startTime = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "endTime":
                        endTime = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "geoLocation":
                        coordinate = new Coordinate.CoordinateObjectMappingFactory().instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(id != null);
            Assertion.eval(thumbnailURL != null);
            Assertion.eval(imageURLs != null);
            Assertion.eval(locations != null);
            Assertion.eval(supplier != null);
            Assertion.eval(price != null);
            Assertion.eval(payableOnline != null);
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
                    payableOnline,
                    payableOnsite,
                    productType,
                    categories,
                    isWishlisted,
                    providerTranslationAttribution,
                    dateRange,
                    coordinate);
        }
    }
}
