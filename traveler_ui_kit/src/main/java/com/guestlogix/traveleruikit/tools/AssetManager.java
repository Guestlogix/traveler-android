package com.guestlogix.traveleruikit.tools;

import android.widget.ImageView;
import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.tools.image.MemoryImageCache;

import java.net.URL;

/**
 * This class will manage assets for Traveler app.
 */
public class AssetManager {

    private static AssetManager localInstance;
    private ImageLoader imageLoader;

    private AssetManager() {
        MemoryImageCache imageCache = new MemoryImageCache();
        imageLoader = new ImageLoader(imageCache);
    }

    public static AssetManager getInstance() {
        if (null == localInstance) {
            localInstance = new AssetManager();
        }
        return localInstance;
    }

    public void loadImage(URL url, ImageView imageView) {
        imageLoader.loadImage(url, imageView);
    }

}

