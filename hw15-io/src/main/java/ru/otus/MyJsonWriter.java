package ru.otus;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

import static ru.otus.TypeChecker.isArray;
import static ru.otus.TypeChecker.isPrimitiveOrString;

class MyJsonWriter {

    String toJsonString(Object object) {
        return toJsonValue(object).toString();
    }

    private JsonValue toJsonValue(Object object) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (object == null)
            return null;
        if (isPrimitiveOrString(object)) {
            return inspectPrimitiveOrString(object);
        } else if (object instanceof Collection) {
            return inspectCollection(object).build();
        } else if (isArray(object) > 0) {
            return inspectArray(object, isArray(object)).build();
        }

        Class<?> classToWrite = object.getClass();
        final var declaredFields = classToWrite.getDeclaredFields();

        try {
            navigateObject(declaredFields, object, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    private void navigateObject(Field[] declaredFields, Object object, JsonObjectBuilder objectBuilder) throws IllegalAccessException {
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            final var modifiers = declaredField.getModifiers();
            final var o = declaredField.get(object);
            if (o != null && !(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {

                int arrayLength = isArray(o);
                if (arrayLength > 0) {
                    objectBuilder.add(declaredField.getName(), inspectArray(o, arrayLength));
                } else if (o instanceof Collection) {
                    objectBuilder.add(declaredField.getName(), inspectCollection(o));
                } else if (isPrimitiveOrString(o)) {
                    objectBuilder.add(declaredField.getName(), inspectPrimitiveOrString(o));
                } else {
                    objectBuilder.add(declaredField.getName(), inspectObject(o));
                }
            } else if (o == null && !(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
                objectBuilder.addNull(declaredField.getName());
            }
        }
    }

    private JsonObjectBuilder inspectObject(Object obj) throws IllegalAccessException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (obj != null) {
            navigateObject(obj.getClass().getDeclaredFields(), obj, objectBuilder);
        }

        return objectBuilder;
    }

    private JsonValue inspectPrimitiveOrString(Object obj) {
        JsonValue jsonValue = null;
        if (obj instanceof Boolean) {
            jsonValue = ((Boolean) obj) ? JsonValue.TRUE : JsonValue.FALSE;
        }
        if (obj instanceof Character) {
            jsonValue = Json.createValue(Character.toString((Character) obj));
        }
        if (obj instanceof String) {
            jsonValue = Json.createValue((String) obj);
        }
        if (obj instanceof Byte) {
            jsonValue = Json.createValue((Byte) obj);
        }
        if (obj instanceof Short) {
            jsonValue = Json.createValue((Short) obj);
        }
        if (obj instanceof Integer) {
            jsonValue = Json.createValue((Integer) obj);
        }
        if (obj instanceof Long) {
            jsonValue = Json.createValue((Long) obj);
        }
        if (obj instanceof Float) {
            jsonValue = Json.createValue((Float) obj);
        }
        if (obj instanceof Double) {
            jsonValue = Json.createValue((Double) obj);
        }

        return jsonValue;
    }

    private JsonArrayBuilder inspectArray(Object o, int length) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        try {
            for (int i = 0; i < length; i++) {
                final var arrayElem = Array.get(o, i);
                if (arrayElem != null) {
                    arrayBuilder.add(toJsonValue(arrayElem));
                } else {
                    arrayBuilder.addNull();
                }
            }
        } catch (IllegalArgumentException ignored) {
        }

        return arrayBuilder;
    }

    private JsonArrayBuilder inspectCollection(Object o) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Object item : (Collection) o) {
            if (item != null) {
                arrayBuilder.add(toJsonValue(item));
            } else {
                arrayBuilder.addNull();
            }
        }

        return arrayBuilder;
    }
}