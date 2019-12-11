package ru.otus.service;

import java.util.Optional;

public interface DBService<T> {

    void save(T objectData);

    void update(T objectData);

    Optional<T> load(long id, Class<T> clazz);

}
