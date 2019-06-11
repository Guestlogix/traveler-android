package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderResult implements Serializable {
    private int total;
    private Date fromDate;
    private Date toDate;
    private SparseArray<Order> orders;
    private HashMap<String, Order> allOrders;

    OrderResult(int total, @NonNull Date fromDate, @Nullable Date toDate, @NonNull SparseArray<Order> orders) {
        this.total = total;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.orders = orders;
        this.allOrders = new HashMap<>();

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.valueAt(i);
            allOrders.put(order.getId(), order);
        }
    }

    public int getTotal() {
        return total;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public SparseArray<Order> getOrders() {
        return orders;
    }

    public boolean isResultEquivalentTo(OrderResult orderResult) {
        return ((this.fromDate != null && orderResult.fromDate.equals(orderResult.fromDate)) ||
                (this.fromDate == null && orderResult.fromDate == null)) &&

                this.toDate.equals(orderResult.toDate) &&
                this.total == orderResult.total;
    }

    public @Nullable OrderResult merge(OrderResult orderResult) {
        if (!this.isResultEquivalentTo(orderResult)) {
            // Not mergable
            return null;
        }

        SparseArray<Order> orders = this.orders.clone();

        for (int i = 0; i < orderResult.orders.size(); i++) {
            orders.put(orderResult.orders.keyAt(i), orderResult.orders.valueAt(i));
        }

        return new OrderResult(this.total, this.fromDate, this.toDate, orders);
    }

    static class OrderResultMappingFactory implements ObjectMappingFactory<OrderResult> {
        @Override
        public OrderResult instantiate(JsonReader reader) throws Exception {
            int offset = 0;
            Date fromDate = null;
            Date toDate = null;
            int total = -1;
            List<Order> ordersArray = null;

            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "skip":
                        offset = reader.nextInt();
                        break;
                    case "from":
                        String dateString = JsonReaderHelper.readString(reader);
                        if (dateString != null)
                            fromDate = DateHelper.parseDate(JsonReaderHelper.readString(reader));
                        break;
                    case "to":
                        // TODO: Rename all JsonReaderHelper methods to reflect convention of 'next[Type]'
                        toDate = DateHelper.parseDate(reader.nextString());
                        break;
                    case "total":
                        total = reader.nextInt();
                        break;
                    case "result":
                        ArrayMappingFactory<Order> factory = new ArrayMappingFactory<>(new Order.OrderMappingFactory());
                        ordersArray = factory.instantiate(reader);
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(total >= 0);
            Assertion.eval(toDate != null);
            Assertion.eval(ordersArray != null);

            SparseArray<Order> orders = new SparseArray<>();

            for (int i = 0; i < ordersArray.size(); i++) {
                orders.put(i + offset, ordersArray.get(i));
            }

            return new OrderResult(total, fromDate, toDate, orders);
        }
    }
}