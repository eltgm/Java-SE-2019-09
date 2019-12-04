package ru.otus;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

import static ru.otus.TypeChecker.*;

class MyJsonWriter {

    String toJson(Object object) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (object == null)
            return null;

        Class<?> classToWrite = object.getClass();
        final var declaredFields = classToWrite.getDeclaredFields();

        try {
            navigateObject(declaredFields, object, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return builder.build().toString();
    }

    private void navigateObject(Field[] declaredFields, Object object, JsonObjectBuilder objectBuilder) throws IllegalAccessException {
        if (!isPrimitive(object.getClass().getSimpleName()) && !(object instanceof String)) {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                final var o = declaredField.get(object);


                int arrayLength = isArray(o);
                if (arrayLength > 0) {
                    objectBuilder.add(declaredField.getName(), inspectArray(o, arrayLength));
                }

                if (o instanceof Collection) {
                    objectBuilder.add(declaredField.getName(), inspectCollection(o));
                }

                if (isPrimitive(o.getClass().getSimpleName())) {
                    objectBuilder.addAll(inspectPrimitive(o, declaredField.getName()));
                } else if (o instanceof String) {
                    objectBuilder.addAll(inspectString(o, declaredField.getName()));
                } else if (arrayLength == -1 && !(o instanceof Collection)) {
                    objectBuilder.add(declaredField.getName(), inspectObject(o));
                }
            }
        } else if (object instanceof String) {
            objectBuilder.addAll(inspectString(object, ""));
        } else {
            objectBuilder.addAll(inspectPrimitive(object, ""));
        }
    }

    private JsonObjectBuilder inspectObject(Object obj) throws IllegalAccessException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (obj != null) {
            navigateObject(obj.getClass().getDeclaredFields(), obj, objectBuilder);
        }

        return objectBuilder;
    }

    private JsonObjectBuilder inspectString(Object obj, String fieldName) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add(fieldName, (String) obj);

        return jsonObjectBuilder;
    }

    private JsonObjectBuilder inspectPrimitive(Object obj, String fieldName) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        switch (obj.getClass().getSimpleName()) {
            case "Boolean":
                jsonObjectBuilder.add(fieldName, (Boolean) obj);
                break;
            case "Character":
                jsonObjectBuilder.add(fieldName, (Character) obj);
                break;
            case "Byte":
                jsonObjectBuilder.add(fieldName, (Byte) obj);
                break;
            case "Short":
                jsonObjectBuilder.add(fieldName, (Short) obj);
                break;
            case "Integer":
                jsonObjectBuilder.add(fieldName, (Integer) obj);
                break;
            case "Long":
                jsonObjectBuilder.add(fieldName, (Long) obj);
                break;
            case "Float":
                jsonObjectBuilder.add(fieldName, (Float) obj);
                break;
            case "Double":
                jsonObjectBuilder.add(fieldName, (Double) obj);
                break;
        }

        return jsonObjectBuilder;
    }

    private JsonArrayBuilder inspectArray(Object o, int length) throws IllegalAccessException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        try {
            final var typeName = o.getClass().getTypeName();
            if (isPrimitiveArray(typeName)) {
                for (int i = 0; i < length; i++) {
                    final var arrayElem = Array.get(o, i);
                    primitiveAdd(arrayBuilder, typeName, arrayElem);
                }
            } else if (isStringArray(o)) {
                for (int i = 0; i < length; i++) {
                    final var arrayElem = Array.get(o, i);
                    if (arrayElem != null) {
                        arrayBuilder.add((String) arrayElem);
                    }
                }
            } else {
                for (int i = 0; i < length; i++) {
                    final var arrayElem = Array.get(o, i);
                    if (arrayElem != null) {
                        JsonObjectBuilder arrayElemBuilder = Json.createObjectBuilder();
                        navigateObject(arrayElem.getClass().getDeclaredFields(), arrayElem, arrayElemBuilder);
                        arrayBuilder.add(arrayElemBuilder);
                    } else {
                        arrayBuilder.addNull();
                    }
                }
            }

        } catch (IllegalArgumentException ignored) {
        }
        return arrayBuilder;
    }

    private void primitiveAdd(JsonArrayBuilder arrayBuilder, String typeName, Object arrayElem) {
        if (arrayElem != null) {
            switch (typeName) {
                case "int[]":
                    arrayBuilder.add((int) arrayElem);
                    break;
                case "long[]":
                    arrayBuilder.add((long) arrayElem);
                    break;
                case "boolean[]":
                    arrayBuilder.add((boolean) arrayElem);
                    break;
                case "float[]":
                    arrayBuilder.add((float) arrayElem);
                    break;
                case "double[]":
                    arrayBuilder.add((double) arrayElem);
                    break;
            }
        }
    }

    private JsonArrayBuilder inspectCollection(Object o) throws IllegalAccessException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Object item : (Collection) o) {
            final var simpleName = item.getClass().getSimpleName();
            if (simpleName.equals("String")) {
                arrayBuilder.add((String) item);
            } else if (isPrimitive(simpleName)) {
                primitiveAdd(arrayBuilder, simpleName, item);
            } else {
                JsonObjectBuilder arrayElemBuilder = Json.createObjectBuilder();
                navigateObject(item.getClass().getDeclaredFields(), item, arrayElemBuilder);
                arrayBuilder.add(arrayElemBuilder);
            }
        }

        return arrayBuilder;
    }
}