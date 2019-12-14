package ru.otus.api.service;

import ru.otus.api.dao.DBTemplate;
import ru.otus.api.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceImpl<T> implements DBService<T> {

  private final DBTemplate<T> dbTemplate;

  public DbServiceImpl(DBTemplate<T> dbTemplate) {

    this.dbTemplate = dbTemplate;
  }

  @Override
  public void saveObject(T object) {
    try (SessionManager sessionManager = dbTemplate.getSessionManager()) {
      sessionManager.beginSession();
      try {
        dbTemplate.saveObject(object);
        sessionManager.commitSession();

      } catch (Exception e) {
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }


  @Override
  public Optional<T> getObject(long id, Class<T> clazz) {
    try (SessionManager sessionManager = dbTemplate.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<T> userOptional = dbTemplate.findById(id, clazz);

        return userOptional;
      } catch (Exception e) {
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

  @Override
  public void updateObject(T object) {
    try (SessionManager sessionManager = dbTemplate.getSessionManager()) {
      sessionManager.beginSession();
      try {
        dbTemplate.updateObject(object);
        sessionManager.commitSession();

      } catch (Exception e) {
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }
}
