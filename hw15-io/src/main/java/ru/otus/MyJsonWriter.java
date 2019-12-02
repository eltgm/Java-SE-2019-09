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
            throw new NullObjectException("Object must not be null!");

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
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    inspectArray(o, arrayLength, arrayBuilder);
                    objectBuilder.add(declaredField.getName(), arrayBuilder);
                }

                if (o instanceof Collection) {
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    inspectCollection(o, arrayBuilder);
                    objectBuilder.add(declaredField.getName(), arrayBuilder);
                }

                if (isPrimitive(o.getClass().getSimpleName())) {
                    inspectPrimitive(o, declaredField.getName(), objectBuilder);
                } else if (o instanceof String) {
                    inspectString(o, declaredField.getName(), objectBuilder);
                } else if (arrayLength == -1 && !(o instanceof Collection)) {
                    inspectObject(o, declaredField.getName(), objectBuilder);
                }
            }
        } /*else if (object instanceof String) {
            inspectString(object, "", objectBuilder);
        } else {
            inspectPrimitive(object, "", objectBuilder);
        }*/
    }

    private void inspectObject(Object obj, String fieldName, JsonObjectBuilder jsonObjectBuilder) throws IllegalAccessException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        if (obj != null) {
            navigateObject(obj.getClass().getDeclaredFields(), obj, objectBuilder);
            jsonObjectBuilder.add(fieldName, objectBuilder);
        }
    }

    private void inspectString(Object obj, String fieldName, JsonObjectBuilder jsonObjectBuilder) {
        jsonObjectBuilder.add(fieldName, (String) obj);
    }

    private void inspectPrimitive(Object obj, String fieldName, JsonObjectBuilder jsonObjectBuilder) {
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
    }

    private void inspectArray(Object o, int length, JsonArrayBuilder arrayBuilder) throws IllegalAccessException {
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

    private void inspectCollection(Object o, JsonArrayBuilder arrayBuilder) throws IllegalAccessException {
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
    }
}
