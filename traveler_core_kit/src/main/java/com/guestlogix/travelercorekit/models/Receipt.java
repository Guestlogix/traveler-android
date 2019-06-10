package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Receipt implements Serializable {
    private String id;
    private String travelerId;
    private Price total;
    private String referenceNumber;
    private String status;
    private List<Product> products;
    private Date createdDate;
    private CustomerContact customerContact;

    @SuppressWarnings("ConstantConditions")
    private Receipt(@NonNull String id, String travelerId, @NonNull Price total, String status, String referenceNumber, @NonNull List<Product> products, @NonNull Date createdDate, @NonNull CustomerContact customerContact) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }

        if (total == null) {
            throw new IllegalArgumentException("total can not be null");
        }

        if (products == null) {
            throw new IllegalArgumentException("products can not be null");
        }

        if (createdDate == null) {
            throw new IllegalArgumentException("createdDate can not be null");
        }

        if (customerContact == null) {
            throw new IllegalArgumentException("customerContact can not be null");
        }

        this.id = id;
        this.travelerId = travelerId;
        this.total = total;
        this.status = status;
        this.referenceNumber = referenceNumber;
        this.products = products;
        this.createdDate = createdDate;
        this.customerContact = customerContact;
    }

    public String getId() {
        return id;
    }

    public String getTravelerId() {
        return travelerId;
    }

    public Price getTotal() {
        return total;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public CustomerContact getCustomerContact() {
        return customerContact;
    }

    static class ReceiptMappingFactory implements ObjectMappingFactory<Receipt> {
        /**
         * Parses a reader object into Receipt model.
         *
         * @param reader object to parse from.
         * @return Receipt model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */

        @Override
        public Receipt instantiate(JsonReader reader) throws Exception {
            String id = null;
            String travelerId = null;
            String status = null;
            Price price = null;
            String referenceNumber = null;
            List<Product> products = null;
            Date createdDate = null;
            CustomerContact customerContact = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "travelerId":
                        travelerId = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "amount":
                        if (JsonToken.NULL != reader.peek()) {
                            price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        } else {
                            price = null;
                            reader.skipValue();
                        }
                        break;
                    case "status":
                        status = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "referenceNumber":
                        referenceNumber = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "createdOn":
                        createdDate = DateHelper.parseISO8601(reader.nextString());
                        break;
                    case "products":
                        if (JsonToken.NULL != reader.peek()) {
                            products = new ArrayMappingFactory<>(new AnyProductMappingFactory()).instantiate(reader);
                        } else {
                            products = null;
                            reader.skipValue();
                        }
                        break;
                    case "customer":
                        if (JsonToken.NULL != reader.peek()) {
                            customerContact = new CustomerContact.CustomerContactObjectMappingFactory().instantiate(reader);
                        } else {
                            customerContact = null;
                            reader.skipValue();
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != id, "id can not be null");
            Assertion.eval(null != price, "price can not be null");
            Assertion.eval(null != products, "products can not be null");
            Assertion.eval(null != createdDate, "createdOn can not be null");
            Assertion.eval(null != customerContact, "customer can not be null");

            return new Receipt(id, travelerId, price, status, referenceNumber, products, createdDate, customerContact);
        }
    }
}
