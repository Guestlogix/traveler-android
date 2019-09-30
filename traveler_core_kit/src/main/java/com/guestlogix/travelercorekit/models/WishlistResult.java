package com.guestlogix.travelercorekit.models;

import android.util.JsonReader;
import androidx.annotation.Nullable;
import com.guestlogix.travelercorekit.utilities.*;

import java.io.Serializable;
import java.util.*;

public class WishlistResult implements Serializable {
    private int skip;
    private int take;
    @Nullable private Date fromDate;
    private Date toDate;
    private int total;
    private Set<CatalogItem> items;

    public WishlistResult(int skip, int take, Date fromDate, Date toDate, int total, Set<CatalogItem> items) {
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

    public Set<CatalogItem> getItems() {
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

        LinkedHashSet<CatalogItem> items = new LinkedHashSet<>(this.items);

        items.addAll(wishlistResult.items);

        return new WishlistResult(this.skip, this.take, this.fromDate, this.toDate, this.total, this.items);
    }

    static class WishlistResultMappingFactory implements ObjectMappingFactory<WishlistResult> {
        @Override
        public WishlistResult instantiate(JsonReader reader) throws Exception {
            int skip = -1;
            int take = -1;
            Date fromDate = null;
            Date toDate = null;
            int total = -1;
            LinkedHashSet<CatalogItem> items = null;

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
                        items = new LinkedHashSet<>(new ArrayMappingFactory<>(
                                new CatalogItem.CatalogItemObjectMappingFactory()).instantiate(reader));
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