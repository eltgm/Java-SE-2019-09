package ru.otus;

public interface AnyClassInterface {
    @Log
    void sayHello(String name, String value);

    @Log
    void sayHello(int id);
}
