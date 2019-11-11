package ru.otus.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestLauncher {
    private final List<Method> beforeEachMethods = new ArrayList<>();
    private final List<Method> testMethods = new ArrayList<>();
    private final List<Method> afterEachMethods = new ArrayList<>();
    private Class className;
    private int failsCounter = 0;

    private TestLauncher() {
    }

    public static void launchTest(Class classForTest) {
        TestLauncher testLauncher = new TestLauncher();
        testLauncher.className = classForTest;
        System.out.println("Test launched from - " + classForTest.getSimpleName());

        final var declaredMethods = classForTest.getDeclaredMethods();
        for (Method method :
                declaredMethods) {
            for (Annotation annotation :
                    method.getDeclaredAnnotations()) {
                switch (annotation.annotationType().getSimpleName()) {
                    case "BeforeEach":
                        testLauncher.beforeEachMethods.add(method);
                        break;
                    case "Test":
                        testLauncher.testMethods.add(method);
                        break;
                    case "AfterEach":
                        testLauncher.afterEachMethods.add(method);
                        break;
                }
            }
        }

        if (!testLauncher.testMethods.isEmpty())
            runTests(testLauncher);
        else
            System.out.println("Class doesn't contains @Test methods!");
    }

    private static void runTests(TestLauncher testLauncher) {
        testLauncher.testMethods.forEach(method -> {
            try {
                final var constructor = testLauncher.className.getConstructor();
                final var newInstance = constructor.newInstance();

                for (Method beforeEachMethods : testLauncher.beforeEachMethods) {
                    beforeEachMethods.invoke(newInstance);
                }

                try {
                    System.out.println("Test for " + method.getName());
                    method.invoke(newInstance);
                } catch (InvocationTargetException error) {
                    error.getTargetException().printStackTrace();
                    testLauncher.failsCounter++;
                }

                for (Method afterEachMethods : testLauncher.afterEachMethods) {
                    afterEachMethods.invoke(newInstance);
                }
            } catch (NoSuchMethodException | IllegalAccessException |
                    InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        showResults(testLauncher);
    }

    private static void showResults(TestLauncher testLauncher) {
        System.out.println();

        System.out.println("*************Test results**************");
        System.out.println(String.format("*Tests failed: %d, passed: %d of %d tests*"
                , testLauncher.failsCounter, testLauncher.testMethods.size() - testLauncher.failsCounter
                , testLauncher.testMethods.size()));
        System.out.println("***************************************");
    }
}
