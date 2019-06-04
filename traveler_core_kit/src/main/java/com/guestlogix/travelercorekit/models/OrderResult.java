package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.TravelerLog;
import com.guestlogix.travelercorekit.utilities.*;
import org.json.JSONException;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class OrderResult implements Serializable {
    private int offset;
    private int limit;
    private Date fromDate;
    private Date toDate;
    private int total;
    private HashMap<Integer, Order> orders;

    private OrderResult(int offset, int limit, Date fromDate, Date toDate, int total, HashMap<Integer, Order> orders) {
        this.offset = offset;
        this.limit = limit;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.total = total;
        this.orders = orders;
    }

    public OrderResult(OrderResult orderResult) {
        this.orders = (HashMap<Integer, Order>) orderResult.orders.clone();
        this.offset = orderResult.offset;
        this.limit = orderResult.limit;
        this.fromDate = orderResult.fromDate;
        this.toDate = orderResult.toDate;
        this.total = orderResult.total;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public int getTotal() {
        return total;
    }

    public HashMap<Integer, Order> getOrders() {
        return orders;
    }

    OrderResult merge(OrderResult orderResult) {
        if (null == orderResult) {
            return new OrderResult(this);
        }

        if (!this.isResultEquivalent(orderResult)) {
            TravelerLog.e("Cannot merge OrderResults. Make sure both instances are result equivalent.");
            return null;
        } else {
            HashMap<Integer, Order> mergedHashMap = new HashMap<>();

            mergedHashMap.putAll(this.orders);
            mergedHashMap.putAll(orderResult.orders);

            return new OrderResult(this.offset, this.limit, this.fromDate, this.toDate, this.total, mergedHashMap);
        }
    }

    private boolean isResultEquivalent(OrderResult orderResult) {
        if (null == orderResult) {
            return false;
        }
        return (((this.fromDate == null && orderResult.fromDate == null) || (this.fromDate != null && this.fromDate.equals(orderResult.fromDate)))
                && ((this.toDate == null && orderResult.toDate == null) || (this.toDate != null && this.toDate.equals(orderResult.toDate)))
                && this.total == orderResult.total);
    }

    public static class OrderResultsObjectMappingFactory implements ObjectMappingFactory<OrderResult> {

        @Override
        public OrderResult instantiate(JsonReader reader) throws ObjectMappingException, IOException {
            String model = "OrderResult";
            String key = "OrderResult";
            try {
                int skip = -1;
                int take = -1;
                Date from = null;
                Date to = null;
                int total = -1;
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
                            String dateFromString = JsonReaderHelper.readString(reader);
                            if (null != dateFromString) {
                                from = DateHelper.parseISO8601(dateFromString);
                            } else {
                                from = null;
                            }
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

                HashMap<Integer, Order> tempOrdersHashMap = new HashMap<>();
                if (skip != -1) {
                    int index = skip;
                    for (Order order : orders) {
                        tempOrdersHashMap.put(index++, order);
                    }
                }
                if (skip < 0) {
                    throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, "Expected a positive int");
                } else if (take < 0) {
                    throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, "Expected a positive int");
                } else if (total < 0) {
                    throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, "Expected a positive int");
                } else {
                    return new OrderResult(skip, take, from, to, total, tempOrdersHashMap);
                }
            } catch (IllegalStateException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_FIELD, model, key, e.getMessage());
            } catch (JSONException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.MISSING_FIELD, model, key, "");
            } catch (ParseException e) {
                throw new ObjectMappingException(ObjectMappingErrorCode.INVALID_DATA, model, key, e.getMessage());
            }
        }
    }
}