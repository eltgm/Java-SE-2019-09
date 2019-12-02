package ru.otus;

import java.lang.reflect.Array;

public final class TypeChecker {
    public static boolean isStringArray(Object o) {
        if (o != null) {
            return o.getClass().getTypeName().equals("String[]");
        }
        return false;
    }

    public static boolean isPrimitiveArray(String type) {
        switch (type) {
            case "boolean[]":
            case "char[]":
            case "byte[]":
            case "short[]":
            case "int[]":
            case "long[]":
            case "float[]":
            case "double[]":
                return true;
        }
        return false;
    }

    public static boolean isPrimitive(String type) {
        switch (type) {
            case "Boolean":
            case "Character":
            case "Byte":
            case "Short":
            case "Integer":
            case "Long":
            case "Float":
            case "Double":
                return true;
        }
        return false;
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
