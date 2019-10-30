package ru.otus;

public class Main {
    public static void main(String[] args) {
        AnyClassInterface anyClass = IoC.createMyClass();
        anyClass.sayHello("Vlad");
    }
}
