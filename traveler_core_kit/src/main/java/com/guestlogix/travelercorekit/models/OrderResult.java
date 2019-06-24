package com.guestlogix.travelercorekit.models;

import android.content.Intent;
import android.util.JsonReader;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.Serializable;
import java.util.*;

public class OrderResult implements Serializable {
    private int total;
    private Date fromDate;
    private Date toDate;
    private HashMap<Integer, Order> orders;
    private HashMap<String, Integer> allOrders;

    OrderResult(int total, @NonNull Date fromDate, @Nullable Date toDate, @NonNull HashMap<Integer, Order> orders) {
        this.total = total;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.orders = orders;
        this.allOrders = new HashMap<>();

        Iterator iterator = orders.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry indexOrderPair = (Map.Entry) iterator.next();
            Order order = (Order) indexOrderPair.getValue();
            Integer index = (Integer) indexOrderPair.getKey();

            allOrders.put(order.getId(), index);
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

    public HashMap<Integer, Order> getOrders() {
        return orders;
    }

    public boolean isResultEquivalentTo(OrderResult orderResult) {
        return ((this.fromDate != null && this.fromDate.equals(orderResult.fromDate)) ||
                (this.fromDate == null && orderResult.fromDate == null)) &&

                this.toDate.equals(orderResult.toDate) &&
                this.total == orderResult.total;
    }

    public @Nullable OrderResult merge(OrderResult orderResult) {
        if (!this.isResultEquivalentTo(orderResult)) {
            // Not mergable
            return null;
        }

        HashMap<Integer, Order> orders = (HashMap<Integer, Order>) this.orders.clone();

        Iterator iterator = orderResult.orders.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry indexOrderPair = (Map.Entry) iterator.next();
            Order order = (Order) indexOrderPair.getValue();
            Integer index = (Integer) indexOrderPair.getKey();

            orders.put(index, order);
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
                        fromDate = DateHelper.parseDate(reader.nextString());
                        break;
                    case "to":
                        String dateString = JsonReaderHelper.nextNullableString(reader);
                        if (dateString != null)
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

            HashMap<Integer, Order> orders = new HashMap<>();

            for (int i = 0; i < ordersArray.size(); i++) {
                orders.put(i + offset, ordersArray.get(i));
            }

            return new OrderResult(total, fromDate, toDate, orders);
        }
    }
}