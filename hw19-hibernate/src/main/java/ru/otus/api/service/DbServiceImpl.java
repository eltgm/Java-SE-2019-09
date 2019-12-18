package ru.otus.api.service;

import ru.otus.api.dao.DBTemplate;

import java.util.Optional;

public class DbServiceImpl<T> implements DBService<T> {

    private final DBTemplate<T> dbTemplate;

    public DbServiceImpl(DBTemplate<T> dbTemplate) {
        this.dbTemplate = dbTemplate;
    }

    @Override
    public void saveObject(T object) {
        dbTemplate.saveObject(object);
    }


    @Override
    public Optional<T> getObject(long id, Class<T> clazz) {
        Optional<T> userOptional = dbTemplate.findById(id, clazz);

        return userOptional;
    }

    @Override
    public void updateObject(T object) {
        dbTemplate.updateObject(object);
    }
}
