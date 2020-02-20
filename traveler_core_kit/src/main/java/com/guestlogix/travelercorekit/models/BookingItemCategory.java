package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.io.Serializable;

public class BookingItemCategory implements Serializable {

    private String id;
    private String title;

    public BookingItemCategory(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static class CategoryObjectMappingFactory implements ObjectMappingFactory<BookingItemCategory> {
        @Override
        public BookingItemCategory instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String id = jsonObject.getString("id");
            String title = jsonObject.getString("label");

            return new BookingItemCategory(id, title);
        }
    }

    @Override
    public String toString() {
        return title;
    }
}
