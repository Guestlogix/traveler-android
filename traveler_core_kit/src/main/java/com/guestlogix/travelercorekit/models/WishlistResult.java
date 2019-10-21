package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WishlistResult implements Serializable {
    private int skip;
    private int take;
    @Nullable private Date fromDate;
    private Date toDate;
    private int total;
    private List<CatalogItem> items;

    WishlistResult(int skip, int take, @Nullable Date fromDate, Date toDate, int total, List<CatalogItem> items) {
        this.skip = skip;
        this.take = take;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.total = total;
        this.items = items;
    }

    public int getSkip() {
        return skip;
    }

    public int getTake() {
        return take;
    }

    @Nullable public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public int getTotal() {
        return total;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public boolean isResultEquivalentTo(WishlistResult wishlistResult) {
        return ((this.fromDate != null && this.fromDate.equals(wishlistResult.fromDate)) ||
                (this.fromDate == null && wishlistResult.fromDate == null)) &&
                this.toDate.equals(wishlistResult.toDate) &&
                this.total == wishlistResult.total;
    }

    public @Nullable WishlistResult merge(WishlistResult wishlistResult) {
        if (!this.isResultEquivalentTo(wishlistResult)) {
            // Not mergable
            return null;
        }

        List<CatalogItem> items = new ArrayList<>(this.items);

        items.addAll(wishlistResult.items);

        return new WishlistResult(this.skip, this.take, this.fromDate, this.toDate, this.total, items);
    }

    /**
     * @param product item to remove
     * @return index that product was removed from; -1 if item is not found
     */
    public int remove(Product product) {
        Iterator<CatalogItem> iterator = items.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            CatalogItem item = iterator.next();
            if (((BookingItem)item).getId().equals(product.getId())) {
                items.remove(item);
                total--;
                return index;
            }
            index++;
        }
        return -1;
    }

    public void add(CatalogItem item, int index) {
        items.add(index, item);
        total++;
    }

    static class WishlistResultMappingFactory implements ObjectMappingFactory<WishlistResult> {
        @Override
        public WishlistResult instantiate(JsonReader reader) throws Exception {
            int skip = -1;
            int take = -1;
            Date fromDate = null;
            Date toDate = null;
            int total = -1;
            List<CatalogItem> items = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "skip":
                        skip = reader.nextInt();
                        break;
                    case "take":
                        take = reader.nextInt();
                        break;
                    case "from":
                        String fromDateString = JsonReaderHelper.nextNullableString(reader);
                        if (fromDateString != null) {
                            fromDate = DateHelper.parseISO8601(fromDateString);
                        }
                        break;
                    case "to":
                        toDate = DateHelper.parseISO8601(reader.nextString());
                        break;
                    case "total":
                        total = reader.nextInt();
                        break;
                    case "result":
                        items = new ArrayList<>(new ArrayMappingFactory<>(
                                new AnyItemMappingFactory()).instantiate(reader));
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }

            reader.endObject();

            Assertion.eval(items != null);

            return new WishlistResult(skip, take, fromDate, toDate, total, items);
        }
    }
}