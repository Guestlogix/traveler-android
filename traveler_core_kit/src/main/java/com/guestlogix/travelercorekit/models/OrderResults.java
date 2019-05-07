package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import android.util.JsonToken;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderResults implements Serializable {
    Integer skip;
    Integer take;
    Date from;
    Date to;
    Integer total;
    List<Order> orders;

    public OrderResults(Integer skip, Integer take, Date from, Date to, Integer total, List<Order> orders) {
        this.skip = skip;
        this.take = take;
        this.from = from;
        this.to = to;
        this.total = total;
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public static class OrderResultsObjectMappingFactory implements ObjectMappingFactory<OrderResults> {

        @Override
        public OrderResults instantiate(JsonReader reader) throws ObjectMappingException, IOException {

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
                return new OrderResults(skip, take, from, to, total, orders);
            } catch (IOException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, "IOException has occurred"));
            } catch (ParseException e) {
                throw new ObjectMappingException(new ObjectMappingError(ObjectMappingErrorCode.INVALID_DATA, e.getMessage()));
            }
        }
    }

    public OrderResults merge(OrderResults orders) {
        return orders;
    }
}