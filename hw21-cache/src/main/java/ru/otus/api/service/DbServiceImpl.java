package ru.otus.api.service;

import ru.otus.api.dao.DBTemplate;
import ru.otus.cachehw.HwCache;

import java.util.Optional;

public class DbServiceImpl<T> implements DBService<T> {
    private final HwCache<String, T> cache;
    private final DBTemplate<T> dbTemplate;

    public DbServiceImpl(HwCache<String, T> cache, DBTemplate<T> dbTemplate) {
        this.cache = cache;
        this.dbTemplate = dbTemplate;
    }

    @Override
    public void saveObject(T object) {
        dbTemplate.saveObject(object);
    }


    @Override
    public Optional<T> getObject(long id, Class<T> clazz) {
        Optional<T> userOptional;
        final var cachedValue = cache.get(String.valueOf(id));
        if (cachedValue == null) {
            userOptional = dbTemplate.findById(id, clazz);
            cache.put(String.valueOf(id), userOptional.get());
        } else
            userOptional = Optional.of(cachedValue);
        return userOptional;
    }

    @Override
    public void updateObject(T object) {
        dbTemplate.updateObject(object);
    }
}
