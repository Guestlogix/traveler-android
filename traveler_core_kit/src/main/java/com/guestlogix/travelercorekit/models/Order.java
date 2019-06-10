package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.Serializable;
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
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id can not be null");
        }

        if (total == null) {
            throw new IllegalArgumentException("total can not be null");
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
        /**
         * Parses a reader object into Order model.
         *
         * @param reader object to parse from.
         * @return order model object from the reader.
         * @throws {@link Exception} if mapping fails due to unexpected token, invalid type, missing required field or unable to parse date type.
         */
        @Override
        public Order instantiate(JsonReader reader) throws Exception {
            String id = null;
            Price price = null;
            String orderNumber = null;
            String status = null;
            List<Product> products = null;
            Date createdDate = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        id = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "amount":
                        if (reader.peek() != JsonToken.NULL) {
                            price = new Price.PriceObjectMappingFactory().instantiate(reader);
                        } else {
                            price = null;
                            reader.skipValue();
                        }
                        break;
                    case "referenceNumber":
                        orderNumber = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "status":
                        status = JsonReaderHelper.nextNullableString(reader);
                        break;
                    case "createdOn":
                        createdDate = DateHelper.parseISO8601(reader.nextString());
                        break;
                    case "products":
                        if (reader.peek() != JsonToken.NULL) {
                            products = new ArrayMappingFactory<>(new AnyProductMappingFactory()).instantiate(reader);
                        } else {
                            products = null;
                            reader.skipValue();
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();

            Assertion.eval(null != id && !id.isEmpty(), "id can not be null");
            Assertion.eval(null != price, "amount can not be null");
            Assertion.eval(null != createdDate, "createdOn can not be null");

            return new Order(id, price, orderNumber, status, products, createdDate);
        }
    }
}
