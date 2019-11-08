package ru.otus;

public class AnyClass implements AnyClassInterface {

    @Override
    public void sayHello(String name, String value) {
        System.out.println("Hello, " + name + "!" + value);
    }

    @Override
    public void sayHello(int id) {
        System.out.println("Hello, " + id + "!");
    }
}
