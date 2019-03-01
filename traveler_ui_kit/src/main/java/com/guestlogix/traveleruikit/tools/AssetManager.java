package com.guestlogix.traveleruikit.tools;

import com.guestlogix.traveleruikit.tools.image.ImageLoader;
import com.guestlogix.traveleruikit.tools.image.MemoryImageCache;

import java.net.URL;

/**
 * This class manages assets for Traveler app.
 */
public class AssetManager {

    private static final AssetManager localInstance = new AssetManager();
    private ImageLoader imageLoader;

    private AssetManager() {
        MemoryImageCache imageCache = new MemoryImageCache();
        imageLoader = new ImageLoader(imageCache);
    }

    public static AssetManager getInstance() {
        return localInstance;
    }

    public void loadImage(URL url, int width, int height, ImageLoader.ImageLoaderCallback imageLoaderCallback) {
        imageLoader.loadImage(url, width, height, imageLoaderCallback);
    }

}

