package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public final class Order implements Serializable {
    private String id;
    private Price total;
    private String referenceNumber;
    private List<Product> products;
    private Date createdDate;
    private OrderStatus status;
    private CustomerContact contact;

    Order(@NonNull String id, @NonNull Price total, String referenceNumber, @NonNull OrderStatus status, @NonNull List<Product> products, @NonNull Date createdDate, @NonNull CustomerContact contact) {
        this.id = id;
        this.total = total;
        this.referenceNumber = referenceNumber;
        this.products = products;
        this.createdDate = createdDate;
        this.status = status;
        this.contact = contact;
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

    public OrderStatus getStatus() {
        return status;
    }

    public CustomerContact getContact() {
        return contact;
    }

    public String getProductTitlesJoinedBy(CharSequence delimiter) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < getProducts().size(); i++) {
            Product product = getProducts().get(i);

            builder.append(product.getTitle());

            if (i != getProducts().size() - 1) {
                builder.append(delimiter);
            }
        }

        return builder.toString();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Order && ((Order) obj).id.equals(this.id));
    }

    static class OrderMappingFactory implements ObjectMappingFactory<Order> {
        @Override
        public Order instantiate(JsonReader reader) throws Exception {
                String id = null;
                Price price = null;
                String orderNumber = null;
                String statusString = null;
                String last4DigitsString = null;
                List<Product> products = null;
                Date createdDate = null;
                OrderStatus status = null;
                CustomerContact contact = null;

                reader.beginObject();

                while (reader.hasNext()) {
                    String key = reader.nextName();

                    switch (key) {
                        case "id":
                            id = reader.nextString();
                            break;
                        case "amount":
                            price = new Price.PriceObjectMappingFactory().instantiate(reader);
                            break;
                        case "referenceNumber":
                            orderNumber = JsonReaderHelper.nextNullableString(reader);
                            break;
                        case "status":
                            statusString = reader.nextString();
                            break;
                        case "createdOn":
                            createdDate = DateHelper.parseISO8601(reader.nextString());
                            break;
                        case "products":
                            products = new ArrayMappingFactory<>(new AnyProductMappingFactory()).instantiate(reader);
                            break;
                        case "last4Digits":
                            last4DigitsString = JsonReaderHelper.nextNullableString(reader);
                            break;
                        case "customer":
                            contact = new CustomerContact.CustomerContactObjectMappingFactory().instantiate(reader);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }

                reader.endObject();

                Assertion.eval(statusString != null);

                switch (statusString) {
                    case "Pending":
                        status = new OrderStatus.Pending();
                        break;
                    case "Confirmed":
                        Assertion.eval(last4DigitsString != null);
                        status = new OrderStatus.Confirmed(new PaymentInfo(last4DigitsString));
                        break;
                    case "Declined":
                        Assertion.eval(last4DigitsString != null);
                        status = new OrderStatus.Declined(new PaymentInfo(last4DigitsString));
                        break;
                    case "UnderReview":
                        Assertion.eval(last4DigitsString != null);
                        status = new OrderStatus.UnderReview(new PaymentInfo(last4DigitsString));
                        break;
                    case "Cancelled":
                        Assertion.eval(last4DigitsString != null);
                        status = new OrderStatus.Cancelled(new PaymentInfo(last4DigitsString));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown status");
                }

                Assertion.eval(id != null);
                Assertion.eval(price != null);
                Assertion.eval(createdDate != null);
                Assertion.eval(contact != null);

                return new Order(id, price, orderNumber, status, products, createdDate, contact);
        }
    }
}
