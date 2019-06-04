package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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
    private Receipt(@NonNull String id, String travelerId, @NonNull Price total, String status, String referenceNumber, @NonNull List<Product> products, @NonNull Date createdDate, @NonNull CustomerContact customerContact) {
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

        @Override
        public Receipt instantiate(JsonReader reader) throws ObjectMappingException {
            String model = "Receipt";
            String key = "Receipt";
            try {
                String id = null;
                String travelerId = null;
                String status = null;
                Price price = null;
                String referenceNumber = null;
                List<Product> products = null;
                Date createdDate = null;
                CustomerContact customerContact = null;

                JsonToken token = reader.peek();

                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }

                reader.beginObject();

                while (reader.hasNext()) {
                    String name = reader.nextName();

                    switch (name) {
                        case "id":
                            id = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "travelerId":
                            travelerId = JsonReaderHelper.readString(reader);
                            break;
                        case "amount":
                            price = new Price.PriceObjectMappingFactory().instantiate(reader);
                            break;
                        case "status":
                            status = JsonReaderHelper.readString(reader);
                            break;
                        case "referenceNumber":
                            referenceNumber = JsonReaderHelper.readString(reader);
                            break;
                        case "createdOn":
                            try {
                                createdDate = DateHelper.parseISO8601(JsonReaderHelper.readNonNullString(reader));
                            } catch (ParseException e) {
                                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, "invalid format");
                            }
                            break;
                        case "products":
                            products = new ArrayMappingFactory<>(new AnyProductMappingFactory()).instantiate(reader);
                            break;
                        case "customer":
                            customerContact = new CustomerContact.CustomerContactObjectMappingFactory().instantiate(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new Receipt(id, travelerId, price, status, referenceNumber, products, createdDate, customerContact);
            } catch (IllegalStateException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, e.getMessage());
            } catch (JSONException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.MISSING_FIELD, model, key, "");
            } catch (IOException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_DATA, model, key, "IOException has occurred");
            }
        }
    }
}
