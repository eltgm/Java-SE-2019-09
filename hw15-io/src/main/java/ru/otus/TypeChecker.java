package ru.otus;

import java.lang.reflect.Array;

public final class TypeChecker {
    private TypeChecker() {
    }

    public static boolean isPrimitiveOrString(Object obj) {
        return obj instanceof Boolean || obj instanceof Character || obj instanceof String
                || obj instanceof Byte || obj instanceof Short || obj instanceof Integer
                || obj instanceof Long || obj instanceof Float || obj instanceof Double;
    }

    public static int isArray(Object o) {
        try {
            if (o != null)
                return Array.getLength(o);
            return -1;
        } catch (IllegalArgumentException e) {
            return -1;
        }
    }
}
