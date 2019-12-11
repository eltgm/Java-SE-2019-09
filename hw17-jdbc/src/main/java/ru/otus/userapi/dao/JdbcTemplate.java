package ru.otus.userapi.dao;

import ru.otus.jdbc.sessionmanager.SessionManager;

import java.util.Optional;

public interface JdbcTemplate<T> {
    Optional<T> findById(long id, Class<T> clazz);

    void create(T objectData);

    void update(T objectData);

    SessionManager getSessionManager();
}
