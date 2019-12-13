package ru.otus;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ObjectCreator<T> {
    public T createObject(Class<T> tClass, Map<String, Object> values) {
        T object = null;
        try {

            object = tClass.getConstructor().newInstance();
            for (String s : values.keySet()) {
                final var declaredField = tClass.getDeclaredField(s);
                declaredField.setAccessible(true);

                declaredField.set(object, values.get(s));
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return object;
    }
}
