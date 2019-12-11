package ru.otus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SQLQueryGenerator {
    public QueryHandler createInsertQuery(Object object) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();
        final var declaredFields = objectClass.getDeclaredFields();
        if (!isSupported(declaredFields))
            throw new ClassNotSupportedException("Classes without @Id does not supported");
        final var queryValues = genQueryValues(object, declaredFields);
        var queryString = genInsertString(objectClass, queryValues);

        return QueryHandler.builder()
                .queryString(queryString)
                .queryValues(queryValues)
                .build();
    }

    public QueryHandler createUpdateQuery(Object object) throws IllegalAccessException {
        Class<?> objectClass = object.getClass();
        final var declaredFields = objectClass.getDeclaredFields();
        if (!isSupported(declaredFields))
            throw new ClassNotSupportedException("Classes without @Id does not supported");
        final var queryValues = genQueryValues(object, declaredFields);
        var queryString = genUpdateString(object, queryValues, declaredFields);

        return QueryHandler.builder()
                .queryString(queryString)
                .queryValues(queryValues)
                .build();
    }

    public QueryHandler createSelectQuery(Class<?> objectClass) {
        final var declaredFields = objectClass.getDeclaredFields();
        if (!isSupported(declaredFields))
            throw new ClassNotSupportedException("Classes without @Id does not supported");
        final var queryValues = genQueryValuesForSelect(declaredFields);

        Field idField = null;
        for (Field field :
                declaredFields) {
            final var idAnnotation = field.getAnnotation(Id.class);
            if (idAnnotation != null) {
                idField = field;
                break;
            }
        }

        var queryString = genSelectString(objectClass, queryValues, idField);

        return QueryHandler.builder()
                .queryString(queryString)
                .queryValues(queryValues)
                .build();
    }

    private String genSelectString(Class<?> objectClass, Map<String, Object> queryValues, Field field) {
        StringBuilder queryBuilder = new StringBuilder("select ");

        for (String s : queryValues.keySet()) {
            queryBuilder.append(s).append(", ");
        }
        if (queryBuilder.charAt(queryBuilder.length() - 1) == ' ') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }

        queryBuilder.append(" from ")
                .append(objectClass.getSimpleName().toLowerCase())
                .append(" where ")
                .append(String.format("%s = ?", field.getName()));

        return queryBuilder.toString();
    }

    private Map<String, Object> genQueryValuesForSelect(Field[] declaredFields) {
        final var queryValues = new HashMap<String, Object>();
        for (Field field :
                declaredFields) {
            queryValues.put(field.getName(), null);
        }
        return queryValues;
    }

    private String genUpdateString(Object object, Map<String, Object> queryValues, Field[] objectFields) throws IllegalAccessException {
        StringBuilder queryBuilder = new StringBuilder(String.format("update %s set (", object.getClass().getSimpleName().toLowerCase()));
        var values = 0;
        for (String s : queryValues.keySet()) {
            queryBuilder.append(s).append(", ");
            values++;
        }

        if (queryBuilder.charAt(queryBuilder.length() - 1) == ' ') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }

        queryBuilder.append(") = (");
        queryBuilder.append("?,".repeat(Math.max(0, values)));
        if (queryBuilder.charAt(queryBuilder.length() - 1) == ',') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }
        queryBuilder.append(") where ");

        for (Field objectField : objectFields) {

            if (objectField.getAnnotation(Id.class) != null) {
                objectField.setAccessible(true);

                queryBuilder.append(objectField.getName()).append("=").append(String.format("%s", objectField.get(object)));
            }
        }

        return queryBuilder.toString();
    }

    private String genInsertString(Class<?> objectClass, Map<String, Object> queryValues) {

        StringBuilder queryBuilder = new StringBuilder(String.format("insert into %s(", objectClass.getSimpleName().toLowerCase()));
        var values = 0;
        for (String s : queryValues.keySet()) {
            queryBuilder.append(s).append(", ");
            values++;
        }

        if (queryBuilder.charAt(queryBuilder.length() - 1) == ' ') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }

        queryBuilder.append(") values (");
        queryBuilder.append("?,".repeat(Math.max(0, values)));
        if (queryBuilder.charAt(queryBuilder.length() - 1) == ',') {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }
        queryBuilder.append(")");
        return queryBuilder.toString();
    }

    private Map<String, Object> genQueryValues(Object object, Field[] declaredFields) throws IllegalAccessException {
        final var queryValues = new HashMap<String, Object>();

        for (Field field :
                declaredFields) {
            field.setAccessible(true);

            var fieldValue = field.get(object);
            if (fieldValue != null) {
                queryValues.put(field.getName(), fieldValue);
            }
        }
        return queryValues;
    }

    private boolean isSupported(Field[] fields) {
        for (Field field :
                fields) {
            final var idAnnotation = field.getAnnotation(Id.class);
            if (idAnnotation != null) {
                return true;
            }
        }
        return false;
    }
}
