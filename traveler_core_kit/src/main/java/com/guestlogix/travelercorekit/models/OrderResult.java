package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

public class OrderResult implements Serializable {
    private Integer offset;
    private Integer limit;
    private Date from;
    private Date to;
    private Integer total;
    private volatile HashMap<Integer, Order> volatileOrders = new HashMap<>();
    private HashMap<Integer, Order> orders = new HashMap<>();

    private OrderResult() {
    }

    private OrderResult(Integer offset, Integer limit, Date from, Date to, Integer total, List<Order> orders) {
        this.offset = offset;
        this.limit = limit;
        this.from = from;
        this.to = to;
        this.total = total;

        int key = offset;
        if (this.orders.size() <= 0) {
            for (Order order : orders) {
                this.orders.put(key++, order);
            }
        } else {
            for (Order order : orders) {
                this.volatileOrders.put(key++, order);
            }
        }

    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public Integer getTotal() {
        return total;
    }

    public HashMap<Integer, Order> getOrders() {
        return orders;
    }

    OrderResult merge(OrderResult orderResult) {
        if (null == orderResult) {
            return this;
        }
        if (!this.isResultEquivalent(orderResult)) {
            throw new IllegalArgumentException("Incompatible orderResult, make sure both orderResults are equivalent.");
        } else {
            for (Map.Entry<Integer, Order> pair : orderResult.volatileOrders.entrySet()) {
                this.orders.put(pair.getKey(), pair.getValue());
            }
            return this;
        }
    }

    OrderResult copy() {

        OrderResult orderResult = new OrderResult();

        HashMap<Integer, Order> orders = new HashMap<>();
        for (Map.Entry<Integer, Order> pair : this.orders.entrySet()) {
            orders.put(pair.getKey(), pair.getValue());
        }

        orderResult.offset = this.offset;
        orderResult.limit = this.limit;
        orderResult.from = this.from;
        orderResult.to = this.to;
        orderResult.total = this.total;
        orderResult.volatileOrders = orders;

        return orderResult;
    }

    public boolean isResultEquivalent(OrderResult orderResult) {
        if (null == orderResult) {
            return false;
        }
        return (this.from.equals(orderResult.from) && this.to.equals(orderResult.to) && this.total.equals(orderResult.total));
    }

    public static class OrderResultsObjectMappingFactory implements ObjectMappingFactory<OrderResult> {

        @Override
        public OrderResult instantiate(JsonReader reader) throws ObjectMappingException, IOException {

            String key;
            try {
                Integer skip = null;
                Integer take = null;
                Date from = null;
                Date to = null;
                Integer total = null;
                List<Order> orders = new ArrayList<>();

                JsonToken token = reader.peek();

                if (token == JsonToken.NULL) {
                    reader.skipValue();
                    return null;
                }

                reader.beginObject();

                while (reader.hasNext()) {
                    key = reader.nextName();

                    switch (key) {
                        case "skip":
                            skip = JsonReaderHelper.readInteger(reader);
                            break;
                        case "take":
                            take = JsonReaderHelper.readInteger(reader);
                            break;
                        case "from":
                            from = DateHelper.parseISO8601(JsonReaderHelper.readString(reader));
                            break;
                        case "to":
                            to = DateHelper.parseISO8601(JsonReaderHelper.readString(reader));
                            break;
                        case "total":
                            total = JsonReaderHelper.readInteger(reader);
                            break;
                        case "result":
                            orders = (new ArrayMappingFactory<>(new Order.OrderMappingFactory())).instantiate(reader);
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();
                return new OrderResult(skip, take, from, to, total, orders);
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            } catch (ParseException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, e.getMessage()));
            }
        }
    }
}