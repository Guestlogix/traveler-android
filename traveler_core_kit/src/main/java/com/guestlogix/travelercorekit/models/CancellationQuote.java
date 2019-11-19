package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;

import com.guestlogix.travelercorekit.utilities.ArrayMappingFactory;
import com.guestlogix.travelercorekit.utilities.Assertion;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CancellationQuote implements Serializable {
    private String id;
    private Price totalRefund;
    private Price cancellationCharge;
    private Date expirationDate;
    private List<ProductCancellationQuote> products;
    private List<CancellationReason> cancellationReasons;
    private Order order;

    CancellationQuote(CancellationQuote.Response response, Order order) {
        this.order = order;
        this.id = response.id;
        this.totalRefund = response.totalRefund;
        this.cancellationCharge = response.cancellationCharge;
        this.expirationDate = response.expirationDate;
        this.products = response.products;
        this.cancellationReasons = response.cancellationReasons;
    }

    public Order getOrder() {
        return order;
    }

    public String getId() {
        return id;
    }

    public Price getTotalRefund() {
        return totalRefund;
    }

    public Price getCancellationCharge() {
        return cancellationCharge;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public List<ProductCancellationQuote> getProducts() {
        return products;
    }

    public List<CancellationReason> getCancellationReasons() {
        return cancellationReasons;
    }

    static class Response {
        String id;
        Price totalRefund;
        Price cancellationCharge;
        Date expirationDate;
        List<ProductCancellationQuote> products;
        List<CancellationReason> cancellationReasons;

        Response(String id,
                 Price totalRefund,
                 Price cancellationCharge,
                 Date expirationDate,
                 List<ProductCancellationQuote> products,
                 List<CancellationReason> cancellationReasons) {
            this.id = id;
            this.totalRefund = totalRefund;
            this.cancellationCharge = cancellationCharge;
            this.expirationDate = expirationDate;
            this.products = products;
            this.cancellationReasons = cancellationReasons;
        }

        static class ResponseObjectMappingFactory implements ObjectMappingFactory<Response> {
            @Override
            public Response instantiate(JsonReader reader) throws Exception {
                Date expirationDate = null;
                String id = null;
                Price totalRefund = null;
                Price cancellationCharge = null;
                List<ProductCancellationQuote> products = null;
                List<CancellationReason> cancellationReasons = null;

                reader.beginObject();

                while (reader.hasNext()) {
                    String key = reader.nextName();

                    switch (key) {
                        case "quoteExpiresOn":
                            expirationDate = DateHelper.parseISO8601(reader.nextString());
                            break;
                        case "id":
                            id = reader.nextString();
                            break;
                        case "totalRefund":
                            totalRefund = new Price.PriceObjectMappingFactory().instantiate(reader);
                            break;
                        case "cancellationCharge":
                            cancellationCharge = new Price.PriceObjectMappingFactory().instantiate(reader);
                            break;
                        case "products":
                            products = new ArrayMappingFactory<>(new ProductCancellationQuote.ProductCancellationQuoteObjectMappingFactory()).instantiate(reader);
                            break;
                        case "cancellationReasons":
                            cancellationReasons = new ArrayMappingFactory<>(new CancellationReason.CancellationReasonsObjectMappingFactory()).instantiate(reader);
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }

                reader.endObject();

                Assertion.eval(expirationDate != null);
                Assertion.eval(id != null);
                Assertion.eval(totalRefund != null);
                Assertion.eval(cancellationCharge != null);
                Assertion.eval(products != null);
                Assertion.eval(cancellationReasons != null);

                return new CancellationQuote.Response(id, totalRefund, cancellationCharge, expirationDate, products, cancellationReasons);
            }
        }
    }
}

