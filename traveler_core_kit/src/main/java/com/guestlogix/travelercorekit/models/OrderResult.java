package com.guestlogix.travelercorekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        public OrderResult instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            int offset = jsonObject.getInt("skip");
            Date fromDate = null;

            if (!jsonObject.isNull("from"))
                fromDate = DateHelper.parseISO8601(jsonObject.getString("from"));

            Date toDate = DateHelper.parseISO8601(jsonObject.getString("to"));
            int total = jsonObject.getInt("total");
            List<Order> ordersArray = new ArrayMappingFactory<>(new Order.OrderMappingFactory()).instantiate(jsonObject.getJSONArray("result").toString());

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