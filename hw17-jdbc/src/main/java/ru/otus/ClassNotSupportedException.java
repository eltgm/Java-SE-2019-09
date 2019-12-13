package ru.otus;

public class ClassNotSupportedException extends RuntimeException {
    public ClassNotSupportedException(String msg) {
        super(msg);
    }
}
