package com.guestlogix.traveleruikit.tools.image;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Implements AssetCache to handle storage and retrieval of Bitmaps.
 */
public class MemoryImageCache implements ImageCache {

    private int cacheSize = 5 * 1024 * 1024; // 5MiB
    private LruCache<String, Bitmap> bitmapCache;

    public MemoryImageCache() {
        bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @Override
    public Bitmap get(String key) {
        return bitmapCache.get(key);
    }

    @Override
    public boolean put(String key, Bitmap asset) {
        bitmapCache.put(key, asset);
        return true;
    }

    @Override
    public boolean clear() {
        bitmapCache.evictAll();
        return true;
    }
}
