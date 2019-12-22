package ru.otus.cachehw;

/**
 * @author sergey
 * created on 14.12.18.
 */
public interface HwListener<V> {
    void notify(String key, V value, String action);
}
