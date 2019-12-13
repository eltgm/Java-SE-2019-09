package ru.otus.service;

import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.userapi.dao.JdbcTemplate;

import java.util.Optional;

public class DbServiceImpl<T> implements DBService<T> {
    private final JdbcTemplate<T> jdbcTemplate;

    public DbServiceImpl(JdbcTemplate<T> jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(T objectData) {
        try (SessionManager sessionManager = jdbcTemplate.getSessionManager()) {
            sessionManager.beginSession();
            try {
                jdbcTemplate.create(objectData);
                sessionManager.commitSession();

            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public void update(T objectData) {
        try (SessionManager sessionManager = jdbcTemplate.getSessionManager()) {
            sessionManager.beginSession();
            try {
                jdbcTemplate.update(objectData);
                sessionManager.commitSession();

            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }


    @Override
    public Optional<T> load(long id, Class<T> clazz) {
        try (SessionManager sessionManager = jdbcTemplate.getSessionManager()) {
            sessionManager.beginSession();
            try {

                return jdbcTemplate.findById(id, clazz);
            } catch (Exception e) {
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

}
