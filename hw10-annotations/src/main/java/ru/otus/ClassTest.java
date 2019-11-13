package ru.otus;

import ru.otus.test.AfterEach;
import ru.otus.test.BeforeEach;
import ru.otus.test.Test;

public class ClassTest {
    private Calculator calculator;

    @BeforeEach
    public void beforeEach() {
        calculator = new Calculator();
        System.out.println("New calculator created");
        throw new Error();
    }

    @Test
    public void testAdd() {
        int sum = 5 + 5;
        final var addTest = calculator.add(5, 5);

        if (sum != addTest)
            throw new AssertionError();
    }

    @Test
    public void testSub() {
        int sub = 6 - 5;
        final var subTest = calculator.sub(6, 5);

        if (sub != subTest)
            throw new AssertionError();
    }

    @AfterEach
    public void afterEach() {
        calculator = null;
        System.out.println("Calculator deleted");
    }
}
