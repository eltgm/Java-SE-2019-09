package ru.otus.cachehw;

/**
 * @author sergey
 * created on 14.12.18.
 */
public interface HwCache<V> {

    void put(String key, V value);

    void remove(String key);

    V get(String key);

    void addListener(HwListener listener);

    void removeListener(HwListener listener);
}
