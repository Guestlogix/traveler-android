package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Receipt implements Serializable {
    private String id;
    private String travelerId;
    private String email;
    private Price total;
    private String orderNumber;
    private String status;
    private List<Product> products;
    private Date createdDate;

    @SuppressWarnings("ConstantConditions")
    private Receipt(@NonNull String id, String travelerId, String email, @NonNull Price total, String status, @NonNull String orderNumber, @NonNull List<Product> products, @NonNull Date createdDate) {
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }

        if (total == null) {
            throw new IllegalArgumentException("total can not be null");
        }

        if (orderNumber == null) {
            throw new IllegalArgumentException("orderNumber can not be null");
        }

        if (products == null) {
            throw new IllegalArgumentException("products can not be null");
        }

        if (createdDate == null) {
            throw new IllegalArgumentException("createdDate can not be null");
        }

        this.id = id;
        this.travelerId = travelerId;
        this.email = email;
        this.total = total;
        this.status = status;
        this.orderNumber = orderNumber;
        this.products = products;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public String getTravelerId() {
        return travelerId;
    }

    public String getEmail() {
        return email;
    }

    public Price getTotal() {
        return total;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    static class ReceiptMappingFactory implements ObjectMappingFactory<Receipt> {

        @Override
        public Receipt instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "Receipt";
            try {
                String id = null;
                String travelerId = null;
                String email = null;
                String status = null;
                Price price = null;
                String orderNumber = null;
                List<Product> products = null;
                Date createdDate = null;

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
                            travelerId = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "email":
                            email = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "amount":
                            price = new Price.PriceObjectMappingFactory().instantiate(reader);
                            break;
                        case "status":
                            status = JsonReaderHelper.readString(reader);
                            break;
                        case "orderNumber":
                            orderNumber = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "createdAt":
                            try {
                                createdDate = DateHelper.parseISO8601(JsonReaderHelper.readNonNullString(reader));
                            } catch (ParseException e) {
                                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "createdAt has invalid format"));
                            }
                            break;
                        case "products":
                            products = new ArrayMappingFactory<>(new AnyProductMappingFactory()).instantiate(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

                return new Receipt(id, travelerId, email, price, status, orderNumber, products, createdDate);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
