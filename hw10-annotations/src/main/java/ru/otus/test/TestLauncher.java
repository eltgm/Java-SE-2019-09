package ru.otus.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestLauncher {
    private final static List<Method> beforeEachMethods = new ArrayList<>();
    private final static List<Method> testMethods = new ArrayList<>();
    private final static List<Method> afterEachMethods = new ArrayList<>();
    private static Class className;
    private static int failsCounter = 0;

    private TestLauncher() {
    }

    public static void launchTest(Class classForTest) {
        className = classForTest;
        System.out.println("Test launched from - " + classForTest.getSimpleName());

        final var declaredMethods = classForTest.getDeclaredMethods();
        for (Method method :
                declaredMethods) {
            for (Annotation annotation :
                    method.getDeclaredAnnotations()) {
                switch (annotation.annotationType().getSimpleName()) {
                    case "BeforeEach":
                        beforeEachMethods.add(method);
                        break;
                    case "Test":
                        testMethods.add(method);
                        break;
                    case "AfterEach":
                        afterEachMethods.add(method);
                        break;
                }
            }
        }

        if (!testMethods.isEmpty())
            runTests();
        else
            System.out.println("Class doesn't contains @Test methods!");
    }

    private static void runTests() {
        testMethods.forEach(method -> {
            try {
                final var constructor = className.getConstructor();
                final var newInstance = constructor.newInstance();

                for (Method beforeEachMethods : beforeEachMethods) {
                    beforeEachMethods.invoke(newInstance);
                }

                try {
                    System.out.println("Test for " + method.getName());
                    method.invoke(newInstance);
                } catch (InvocationTargetException error) {
                    error.getTargetException().printStackTrace();
                    failsCounter++;
                }

                for (Method afterEachMethods : afterEachMethods) {
                    afterEachMethods.invoke(newInstance);
                }
            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        showResults();
    }

    private static void showResults() {
        System.out.println();

        System.out.println("*************Test results**************");
        System.out.println(String.format("*Tests failed: %d, passed: %d of %d tests*"
                , failsCounter, testMethods.size() - failsCounter, testMethods.size()));
        System.out.println("***************************************");
    }
}
