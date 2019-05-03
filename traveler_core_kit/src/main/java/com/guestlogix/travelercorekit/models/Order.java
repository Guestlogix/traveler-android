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

public class Order implements Serializable {
    private String id;
    private Price total;
    private String referenceNumber;
    private List<Product> products;
    private Date createdDate;
    private String status;

    @SuppressWarnings("ConstantConditions")
    private Order(@NonNull String id, @NonNull Price total, String referenceNumber, String status, @NonNull List<Product> products, @NonNull Date createdDate) {
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

        this.id = id;
        this.total = total;
        this.referenceNumber = referenceNumber;
        this.status = status;
        this.products = products;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    static class OrderMappingFactory implements ObjectMappingFactory<Order> {

        @Override
        public Order instantiate(JsonReader reader) throws ObjectMappingException {
            String key = "Order";
            try {
                String id = null;
                Price price = null;
                String orderNumber = null;
                String status = null;
                List<Product> products = null;
                Date createdDate = null;

                JsonToken token = reader.peek();
                if (JsonToken.NULL == token) {
                    reader.skipValue();
                    return null;
                }
                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "id":
                            id = JsonReaderHelper.readNonNullString(reader);
                            break;
                        case "amount":
                            price = new Price.PriceObjectMappingFactory().instantiate(reader);
                            break;
                        case "referenceNumber":
                            orderNumber = JsonReaderHelper.readString(reader);
                            break;
                        case "status":
                            status = JsonReaderHelper.readString(reader);
                            break;
                        case "createdOn":
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

                return new Order(id, price, orderNumber, status, products, createdDate);
            } catch (IllegalArgumentException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, String.format(e.getMessage(), key)));
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            }
        }
    }
}
