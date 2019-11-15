package com.guestlogix.travelercorekit.models;

import com.guestlogix.travelercorekit.utilities.JSONObjectGLX;
import com.guestlogix.travelercorekit.utilities.ObjectMappingFactory;

import java.net.URL;

public class ParkingItem implements CatalogItem<ParkingProduct> {
    private String title;
    private String subTitle;
    private URL imageURL;
    private ParkingProduct parkingProduct;

    ParkingItem(
            String title,
            String subTitle,
            URL imageURL,
            ParkingProduct parkingProduct) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageURL = imageURL;
        this.parkingProduct = parkingProduct;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSubtitle() {
        return subTitle;
    }

    @Override
    public URL getImageUrl() {
        return imageURL;
    }

    @Override
    public ParkingProduct getItemResource() {
        return parkingProduct;
    }

    public boolean isAvailable() {
        // A ParkingItem that is seen is always available
        return true;
    }

    static class ParkingItemObjectMappingFactory implements ObjectMappingFactory<ParkingItem> {
        @Override
        public ParkingItem instantiate(String rawResponse) throws Exception {
            JSONObjectGLX jsonObject = new JSONObjectGLX(rawResponse);

            String title = jsonObject.getNullableString("title");
            String subTitle = jsonObject.getNullableString("subTitle");
            URL thumbnail = null;
            if (!jsonObject.isNull("thumbnail"))
                thumbnail = new URL(jsonObject.getString("thumbnail"));
            ParkingProduct parkingProduct = new ParkingProduct.ParkingProductObjectMappingFactory().instantiate(rawResponse);

            return new ParkingItem(
                    title,
                    subTitle,
                    thumbnail,
                    parkingProduct);

        }
    }
}
