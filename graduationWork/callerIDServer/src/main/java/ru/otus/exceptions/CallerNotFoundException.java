package ru.otus.exceptions;

public class CallerNotFoundException extends RuntimeException {
    public CallerNotFoundException(String message) {
        super(message);
    }
}
