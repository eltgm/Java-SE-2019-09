package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

class IoC {
    private static final List<Method> methodList = new ArrayList<>();
    static AnyClassInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new AnyClass());
        return (AnyClassInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{AnyClassInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final AnyClassInterface myClass;

        DemoInvocationHandler(AnyClassInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodList.contains(method)) {
                System.out.println("executed method:" + method + ", param: " + args[0]);
            } else {
                final var annotation = method.getAnnotation(Log.class);
                if (annotation != null) {
                    methodList.add(method);
                    System.out.println("executed method:" + method + ", param: " + args[0]);
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
