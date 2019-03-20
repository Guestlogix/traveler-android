package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
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
    private String orderNumber;
    private List<Product> products;
    private Date createdDate;

    @SuppressWarnings("ConstantConditions")
    private Order(@NonNull String id, @NonNull Price total, @NonNull String orderNumber, @NonNull List<Product> products, @NonNull Date createdDate) {
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
        this.total = total;
        this.orderNumber = orderNumber;
        this.products = products;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
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

    static class OrderMappingFactory implements ObjectMappingFactory<Order> {

        @Override
        public Order instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            String id = null;
            Price price = null;
            String orderNumber = null;
            List<Product> products = null;
            Date createdDate = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "id":
                        id = JsonReaderHelper.readNonNullString(reader);
                        break;
                    case "amount":
                        price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        break;
                    case "orderNumber":
                        orderNumber = JsonReaderHelper.readNonNullString(reader);
                        break;
                    case "createdAt":
                        try {
                            createdDate = DateHelper.parseISO8601(JsonReaderHelper.readNonNullString(reader));
                        } catch (ParseException e) {
                            throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_FORMAT, "createdAt has invalid format"));
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

            if (id == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "payload must have a non-null 'id' field"));
            }

            if (price == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "payload must have a non-null 'price' field"));
            }

            if (orderNumber == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "payload must have a non-null 'orderNumber' field"));
            }

            if (products == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "payload must have a non-null 'products' field"));
            }

            if (createdDate == null) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.EMPTY_FIELD, "payload must have a non-null 'createdAt' field"));
            }

            return new Order(id, price, orderNumber, products, createdDate);
        }
    }
}
