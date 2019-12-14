package ru.otus.api.service;

import java.util.Optional;

public interface DBService<T> {

  void saveObject(T object);

  Optional<T> getObject(long id, Class<T> clazz);

  void updateObject(T object);
}
