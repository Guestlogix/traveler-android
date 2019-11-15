package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;

import java.io.Serializable;
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
        public Order instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            Price price = new Price.PriceObjectMappingFactory().instantiate(jsonObject.getJSONObject("amount").toString());
            String orderNumber = jsonObject.getNullableString("referenceNumber");
            String statusString = jsonObject.getString("status");
            String last4DigitsString = jsonObject.getNullableString("last4Digits");
            List<Product> products = new PurchasedProductListMappingFactory().instantiate(jsonObject.getJSONArray("products").toString());
            Date createdDate = DateHelper.parseISO8601(jsonObject.getString("createdOn"));
            CustomerContact contact =  new CustomerContact.CustomerContactObjectMappingFactory().instantiate(jsonObject.getString("customer"));

            OrderStatus status = null;

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
