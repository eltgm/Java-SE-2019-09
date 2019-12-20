package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private final List<HwListener<K, V>> listeners = new ArrayList<>();
    private final Map<K, V> cache = new WeakHashMap<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(kvHwListener ->
                kvHwListener.notify(key, value, "PUT"));
    }

    @Override
    public void remove(K key) {
        final var removedValue = cache.remove(key);
        listeners.forEach(kvHwListener ->
                kvHwListener.notify(key, removedValue, "REMOVE"));
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener listener) {
        listeners.remove(listener);
    }
}
