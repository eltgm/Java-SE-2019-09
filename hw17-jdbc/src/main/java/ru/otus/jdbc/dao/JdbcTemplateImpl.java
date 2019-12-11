package ru.otus.jdbc.dao;


import ru.otus.ObjectCreator;
import ru.otus.SQLQueryGenerator;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManager;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.userapi.dao.JdbcTemplate;
import ru.otus.userapi.dao.UserDaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {
    private final ObjectCreator<T> objectCreator = new ObjectCreator<>();
    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;
    private final SQLQueryGenerator sqlQueryGenerator = new SQLQueryGenerator();

    public JdbcTemplateImpl(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }


    @Override
    public Optional<T> findById(long id, Class<T> clazz) {
        try {
            final var queryHandler = sqlQueryGenerator.createSelectQuery(clazz);
            final var updatedHandler = dbExecutor.selectRecord(getConnection(), queryHandler, id, resultSet -> {
                try {
                    if (resultSet.next()) {
                        for (String s : queryHandler.getQueryValues().keySet()) {
                            queryHandler.getQueryValues().put(s, resultSet.getObject(s));
                        }

                        return queryHandler;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });
            return Optional.of(objectCreator.createObject(clazz, updatedHandler.get().getQueryValues()));

        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public void create(T objectData) {
        try {
            final var insertQuery = sqlQueryGenerator.createInsertQuery(objectData);
            dbExecutor.insertRecord(getConnection(), insertQuery);
        } catch (Exception e) {

            throw new UserDaoException(e);
        }
    }

    @Override
    public void update(T objectData) {
        try {
            final var insertQuery = sqlQueryGenerator.createUpdateQuery(objectData);
            dbExecutor.updateRecord(getConnection(), insertQuery);
        } catch (Exception e) {

            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
