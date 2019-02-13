package com.guestlogix.traveleruikit.tools;

/**
 * A generic Asset Cache. Each type of asset will implement its own cache storage and retrieval mechanism depending on its type.
 * @param <T>
 */
public interface AssetCache<T> {
    T get(String key);

    boolean put(String key, T asset);

    boolean clear();
}
