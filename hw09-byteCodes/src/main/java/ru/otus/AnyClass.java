package ru.otus;

public class AnyClass implements AnyClassInterface {

    @Override
    public void sayHello(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
