package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<V> implements HwCache<V> {
    //Надо реализовать эти методы
    private final List<HwListener<V>> listeners = new ArrayList<>();
    private final Map<String, V> cache = new WeakHashMap<>();

    @Override
    public void put(String key, V value) {
        cache.put(key, value);
        listeners.forEach(kvHwListener ->
                kvHwListener.notify(key, value, "PUT"));
    }

    @Override
    public void remove(String key) {
        final var removedValue = cache.remove(key);
        listeners.forEach(kvHwListener ->
                kvHwListener.notify(key, removedValue, "REMOVE"));
    }

    @Override
    public V get(String key) {
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
