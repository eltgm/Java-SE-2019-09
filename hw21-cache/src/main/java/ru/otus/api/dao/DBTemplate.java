package ru.otus.api.dao;

import ru.otus.api.sessionmanager.SessionManager;

import java.util.Optional;

public interface DBTemplate<T> {
  Optional<T> findById(long id, Class<T> clazz);

  void saveObject(T object);

  void updateObject(T object);

  SessionManager getSessionManager();
}
