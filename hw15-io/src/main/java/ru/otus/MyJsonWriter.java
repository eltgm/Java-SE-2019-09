package ru.otus;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

import static ru.otus.TypeChecker.*;

class MyJsonWriter {

    String toJson(Object object) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (object == null)
            return null;
        try {
            if (isPrimitive(object.getClass().getSimpleName())) {
                return inspectPrimitive(object).toString();
            } else if (object instanceof String) {
                return inspectString(object).toString();
            } else if (object instanceof Collection) {
                return inspectCollection(object).build().toString();
            } else if (isArray(object) > 0) {

                return inspectArray(object, isArray(object)).build().toString();

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


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
                    objectBuilder.add(declaredField.getName(), inspectPrimitive(o));
                } else if (o instanceof String) {
                    objectBuilder.add(declaredField.getName(), inspectString(o));
                } else if (arrayLength == -1 && !(o instanceof Collection)) {
                    objectBuilder.add(declaredField.getName(), inspectObject(o));
                }
            }
        } else if (object instanceof String) {
            objectBuilder.add("", inspectString(object));
        } else {
            objectBuilder.add("", inspectPrimitive(object));
        }
    }

    private JsonObjectBuilder inspectObject(Object obj) throws IllegalAccessException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (obj != null) {
            navigateObject(obj.getClass().getDeclaredFields(), obj, objectBuilder);
        }

        return objectBuilder;
    }

    private JsonValue inspectString(Object obj) {
        JsonValue jsonValue = Json.createValue((String) obj);

        return jsonValue;
    }

    private JsonValue inspectPrimitive(Object obj) {
        JsonValue jsonValue;
        switch (obj.getClass().getSimpleName()) {
            case "Boolean":
                if ((Boolean) obj) {
                    jsonValue = Json.createValue("true");
                } else {
                    jsonValue = Json.createValue("false");
                }
                break;
            case "Character":
                jsonValue = Json.createValue(Character.toString((Character) obj));
                break;
            case "Byte":
                jsonValue = Json.createValue((Byte) obj);
                break;
            case "Short":
                jsonValue = Json.createValue((Short) obj);
                break;
            case "Integer":
                jsonValue = Json.createValue((Integer) obj);
                break;
            case "Long":
                jsonValue = Json.createValue((Long) obj);
                break;
            case "Float":
                jsonValue = Json.createValue((Float) obj);
                break;
            case "Double":
                jsonValue = Json.createValue((Double) obj);
                break;
            default:
                jsonValue = null;
        }

        return jsonValue;
    }

    private JsonArrayBuilder inspectArray(Object o, int length) throws IllegalAccessException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        try {
            final var typeName = o.getClass().getTypeName();
            if (isPrimitiveArray(typeName)) {
                for (int i = 0; i < length; i++) {
                    final var arrayElem = Array.get(o, i);
                    arrayBuilder.add(inspectPrimitive(arrayElem));
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
                arrayBuilder.add(inspectPrimitive(item));
            } else {
                JsonObjectBuilder arrayElemBuilder = Json.createObjectBuilder();
                navigateObject(item.getClass().getDeclaredFields(), item, arrayElemBuilder);
                arrayBuilder.add(arrayElemBuilder);
            }
        }

        return arrayBuilder;
    }
}