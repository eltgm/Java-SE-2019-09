package ru.otus.jdbc;

import ru.otus.QueryHandler;
import ru.otus.ValuesSetter;

import java.sql.*;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutor<T> {
    public void insertRecord(Connection connection, QueryHandler queryHandler) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(queryHandler.getQueryString(), Statement.RETURN_GENERATED_KEYS)) {
            var valuesSetter = new ValuesSetter(pst);
            final var queryValues = queryHandler.getQueryValues();
            var idx = 1;
            for (String s : queryValues.keySet()) {
                valuesSetter.setValue(queryValues.get(s), idx);
                idx++;
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
            }
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            throw ex;
        }
    }

    public void updateRecord(Connection connection, QueryHandler queryHandler) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(queryHandler.getQueryString(), Statement.RETURN_GENERATED_KEYS)) {
            var valuesSetter = new ValuesSetter(pst);
            final var queryValues = queryHandler.getQueryValues();
            var idx = 1;
            for (String s : queryValues.keySet()) {
                valuesSetter.setValue(queryValues.get(s), idx);
                idx++;
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
            }
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            throw ex;
        }
    }

    public Optional<QueryHandler> selectRecord(Connection connection, QueryHandler queryHandler, long id, Function<ResultSet, QueryHandler> rsHandler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(queryHandler.getQueryString())) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }


}
