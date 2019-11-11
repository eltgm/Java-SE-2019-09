package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class IoC {
    private static final List<Method> methodList = new ArrayList<>();
    private static Class<?> clazz = AnyClassInterface.class;

    static AnyClassInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new AnyClass());
        final var declaredAnnotation = clazz.getDeclaredAnnotation(Log.class);
        if (declaredAnnotation != null) {
            cacheMethodsFromClass();
        } else {
            cacheMethods();
        }
        return (AnyClassInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{AnyClassInterface.class}, handler);
    }

    private static void cacheMethodsFromClass() {
        methodList.addAll(Arrays.asList(clazz.getMethods()));
    }

    private static void cacheMethods() {
        for (Method method :
                clazz.getMethods()) {
            final var annotation = method.getAnnotation(Log.class);
            if (annotation != null) {
                methodList.add(method);
            }
        }
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final AnyClassInterface myClass;

        DemoInvocationHandler(AnyClassInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodList.contains(method)) {
                System.out.println("executed method:" + method + ", params: ");
                for (Object s : args) {
                    System.out.println(s);
                }
            }

            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }

}
